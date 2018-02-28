package itomy.sigterra.service;

import itomy.sigterra.domain.*;
import itomy.sigterra.repository.*;
import itomy.sigterra.service.dto.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.net.URI;
import java.util.*;

/**
 * Created by alexander on 2/2/17.
 */

@Service
@Transactional
public class CardletService {

    public static final int MAX_ALLOWED_PROFILE_ICON_SIZE = 20 * 1024 * 1024;
    private final Logger log = LoggerFactory.getLogger(CardletService.class);
    @Inject
    BusinessRepository businessRepository;
    @Inject
    private CardletRepository cardletRepository;
    @Inject
    private AWSS3BucketService awss3BucketService;
    @Inject
    private UserService userService;
    @Inject
    private UserRepository userRepository;
    @Inject
    private InputPropertiesRepository inputPropertiesRepository;
    @Inject
    private ItemDataRepository itemDataRepository;
    @Inject
    private ItemRepository itemRepository;
    @Inject
    private TabTypeRepository tabTypeRepository;

    @Inject
    private EventService eventService;

    public List<UserCardletDTO> userCardlets(){
        List<Cardlet> caedletList = cardletRepository.findByUserIsCurrentUser();
        List<UserCardletDTO> usetCardletDTOs = new ArrayList<>();
        for (Cardlet cardlet : caedletList) {

            UserCardletDTO userCardletDTO = createUserCatdletDTO(cardlet);
            usetCardletDTOs.add(userCardletDTO);

        }

        return usetCardletDTOs;
    }

    public UserCardletDTO createUserCatdletDTO(Cardlet cardlet){
        UserCardletDTO userCardletDTO = new UserCardletDTO();
        userCardletDTO.setCardletName(cardlet.getName());
        userCardletDTO.setId(cardlet.getId());
        Set<Business> businesses = cardlet.getBusinesses();
        Set<Item> items = cardlet.getItems();
        List<CardletTab> tabs = new ArrayList<>();
        for (Business business : businesses) {
            CardletTab cardletTab = new CardletTab();
            cardletTab.setId(business.getId());
            cardletTab.setName(business.getName());
            cardletTab.setPosition(business.getPisition());
            cardletTab.setUserName(business.getUserName());
            cardletTab.setUserEmail(business.getUserEmail());
            cardletTab.setPhone(business.getPhone());
            cardletTab.setAddress(business.getAddress());
            cardletTab.setCompany(business.getCompany());
            cardletTab.setSite(business.getSite());
            cardletTab.setJob(business.getJob());
            cardletTab.setPhoto(business.getPhoto());

            BusinessSocialLinks links = new BusinessSocialLinks();
            links.setTwitter(business.getTwitter());
            links.setFacebook(business.getFacebook());
            links.setGoogle(business.getGoogle());
            links.setLinkedin(business.getLinkedIn());

            cardletTab.setSocialLinks(links);

            CardletLayout cardletLayout = new CardletLayout();
            cardletLayout.setMainColor(business.getMainColor());
            cardletLayout.setSecondaryColor(business.getColor());
            cardletLayout.setTabId(business.getTabType().getId());
            cardletLayout.setUrl(business.getTabType().getPath());

            cardletTab.setLayout(cardletLayout);
            cardletTab.setTabType(Integer.valueOf(business.getTabType().getType()));
            tabs.add(cardletTab);

        }

        for (Item item : items) {
            CardletTab cardletTabItem = new CardletTab();
            cardletTabItem.setId(item.getId());
            cardletTabItem.setName(item.getName());
            cardletTabItem.setPosition(item.getPisition());
            List <ItemModel> itemModels = new ArrayList<>();
            Set<ItemData> itemDatas = item.getItemData();

            for (ItemData itemData : itemDatas) {
                ItemModel itemModel = new ItemModel();
                itemModel.setId(itemData.getId());
                itemModel.setDescription(itemData.getDescription());
                itemModel.setImage(itemData.getFirstImage());
                itemModel.setImage2(itemData.getSecondImage());
                itemModel.setImage3(itemData.getThirdImage());
                itemModel.setName(itemData.getName());
                itemModel.setIndex(itemData.getTabIndex());
                itemModel.setPosition(itemData.getPosition());
                itemModel.setLink(itemData.getLink());
                itemModel.setPDF(itemData.isPDF());
                itemModels.add(itemModel);

            }

            cardletTabItem.setItems(itemModels);

            CardletLayout cardletLayout = new CardletLayout();
            cardletLayout.setMainColor(item.getMainColor());
            cardletLayout.setSecondaryColor(item.getColor());
            cardletLayout.setTabId(item.getTabType().getId());
            cardletLayout.setUrl(item.getTabType().getPath());

            cardletTabItem.setLayout(cardletLayout);
            cardletTabItem.setTabType(Integer.valueOf(item.getTabType().getType()));
            tabs.add(cardletTabItem);

        }
        Collections.sort(tabs, Comparator.comparing(CardletTab::getPosition));
        userCardletDTO.setTabs(tabs);
        return userCardletDTO;
    }

    public UserCardletDTO getCardlet(Long id, Boolean auth){
        Cardlet cardlet = null;
        if(auth) {
            cardlet = cardletRepository.findOneByIdAndUserIsCurrentUser(id);
        }else {
            cardlet = cardletRepository.findOne(id);
        }
        UserCardletDTO userCardletDTO = null;

        if(cardlet != null)
            userCardletDTO =  createUserCatdletDTO(cardlet);

       return userCardletDTO;
    }

    public JSONObject fileUploading(MultipartFile file, String id, String name, Boolean upladType) throws JSONException {
        JSONObject successObject = new JSONObject();
        if(file != null && !file.isEmpty()) {
            if(file.getSize() > MAX_ALLOWED_PROFILE_ICON_SIZE) {
                successObject.put("success", false);
                successObject.put("message", "File is too big. Max allowed file size is 50Mb");
                // TODO: 1/9/17 Maybe need to change it to not OK status
            }
            String fileName = file.getName();
            String mimeType = file.getContentType();
            if (mimeType.startsWith("image/") || mimeType.startsWith("application/pdf")) {
                URI url;
                if(upladType) {
                    url = awss3BucketService.uploadSignatureImage(file, id, name);
                }else{
                    url = awss3BucketService.uploadBusinessImage(file, id);
                }
                if(url == null) {
                    successObject.put("success", false);
                    successObject.put("message", "Unable to fetch the file from S3 bucket.");
                } else {
                    successObject.put("success", true);
                    successObject.put("url", url);
                }
            }else{
                successObject.put("success", false);
                successObject.put("message", "Invalid file type");
            }


        } else {
            successObject.put("success", false);
            successObject.put("message", "File is empty or NULL");
            // TODO: 1/9/17 Maybe need to change it to not OK status
        }
        return successObject;

    }


    public List<UserCardletDTO> deleteCardlet(Long id){

        Cardlet cardlet = cardletRepository.findOne(id);
        Set<Business> businesses = cardlet.getBusinesses();
        Set<Item> items = cardlet.getItems();
        for (Item item : items) {
            Set<ItemData> itemDatas = item.getItemData();
            for (ItemData itemData : itemDatas) {
                if(itemData.getDescription() != null)
                inputPropertiesRepository.delete(itemData.getDescription());
                if(itemData.getName() != null)
                inputPropertiesRepository.delete(itemData.getName());
                itemDataRepository.delete(itemData);
            }
            itemRepository.delete(item);
        }
        for (Business business : businesses) {
            if(business.getAddress() != null)
            inputPropertiesRepository.delete(business.getAddress());
            if(business.getCompany() != null)
            inputPropertiesRepository.delete(business.getCompany());
            if(business.getJob() != null)
            inputPropertiesRepository.delete(business.getJob());
            if(business.getPhone() != null)
            inputPropertiesRepository.delete(business.getPhone());
            if(business.getSite() != null)
            inputPropertiesRepository.delete(business.getSite());
            if(business.getUserEmail() != null)
            inputPropertiesRepository.delete(business.getUserEmail());
            if(business.getUserName() != null)
            inputPropertiesRepository.delete(business.getUserName());
            businessRepository.delete(business);
        }
        cardletRepository.delete(cardlet);

        List<UserCardletDTO> userCardletDTOs = userCardlets();
        return userCardletDTOs;
    }




    public UserCardletDTO createCardlet(UserCardletDTO cardletDTO, boolean update, Long id, boolean isFirstLogin, boolean isClone) {

        log.debug("REST request to get a page of Cardlets22");
        Cardlet cardlet = new Cardlet();
        if(update){
            cardlet.setId(cardletDTO.getId());
        }
        cardlet.setName(cardletDTO.getCardletName());
        User user = null;
        if(isFirstLogin){
            user = userRepository.findOneById(id);
        }else{
            user = userService.getUserWithAuthorities();
        }
        cardlet.setUser(user);
        Set<Business> businesses  = new HashSet<>();
        Set <Item> items = new HashSet<>();
        List<CardletTab> tabs = cardletDTO.getTabs();

        cardlet = cardletRepository.save(cardlet);

        for (CardletTab tab : tabs) {
            if(tab.getTabType().equals(1)) {
                Business business = new Business();
                if(update){
                    business.setId(tab.getId());
                }
                business.setName(tab.getName());
                if (tab.getUserEmail() != null) {
                    if(isClone){
                        tab.getUserEmail().setId(null);
                    }
                    inputPropertiesRepository.save(tab.getUserEmail());
                    business.setUserEmail(tab.getUserEmail());
                }
                business.setPisition(tab.getPosition());
                business.setMainColor(tab.getLayout().getMainColor());
                business.setColor(tab.getLayout().getSecondaryColor());
                business.setCardlet(cardlet);
                business.setPhoto(tab.getPhoto());
                if (tab.getUserName() != null) {
                    if(isClone){
                        tab.getUserName().setId(null);
                    }
                    inputPropertiesRepository.save(tab.getUserName());
                    business.setUserName(tab.getUserName());
                }
                if (tab.getPhone() != null) {
                    if(isClone){
                        tab.getPhone().setId(null);
                    }
                    inputPropertiesRepository.save(tab.getPhone());
                    business.setPhone(tab.getPhone());
                }
                if (tab.getAddress() != null) {
                    if(isClone){
                        tab.getAddress().setId(null);
                    }
                    inputPropertiesRepository.save(tab.getAddress());
                    business.setAddress(tab.getAddress());
                }
                if (tab.getCompany() != null) {
                    if(isClone){
                        tab.getCompany().setId(null);
                    }
                    inputPropertiesRepository.save(tab.getCompany());
                    business.setCompany(tab.getCompany());
                }
                if (tab.getSite() != null){
                    if(isClone){
                        tab.getSite().setId(null);
                    }
                    inputPropertiesRepository.save(tab.getSite());
                    business.setSite(tab.getSite());
                }
                if (tab.getJob() != null) {
                    if(isClone){
                        tab.getJob().setId(null);
                    }
                    inputPropertiesRepository.save(tab.getJob());
                    business.setJob(tab.getJob());
                }
                if (tab.getSocialLinks() != null) {

                    business.setTwitter(tab.getSocialLinks().getTwitter());
                    business.setFacebook(tab.getSocialLinks().getFacebook());
                    business.setGoogle(tab.getSocialLinks().getGoogle());
                    business.setLinkedIn(tab.getSocialLinks().getLinkedin());
                }
                business.setTabType(tabTypeRepository.findOne(tab.getLayout().getTabId()));
                businesses.add(business);
                businessRepository.save(business);
            }else if(tab.getTabType().equals(2) || tab.getTabType().equals(3) || tab.getTabType().equals(4)){
                Item item = new Item();
                if(update){
                    item.setId(tab.getId());
                }
                item.setName(tab.getName());
                item.setPisition(tab.getPosition());
                item.setColor(tab.getLayout().getSecondaryColor());
                item.setMainColor(tab.getLayout().getMainColor());
                item.setCardlet(cardlet);
                item.setTabType(tabTypeRepository.findOne(tab.getLayout().getTabId()));
                item = itemRepository.save(item);
                List<ItemModel> inputModels = tab.getItems();
                Set<ItemData> itemDatas = new HashSet<>();
                for (ItemModel inputModel : inputModels) {
                    ItemData itemData = new ItemData();
                    if(inputModel.getDescription() != null) {
                        if(isClone){
                            inputModel.getDescription().setId(null);
                        }
                        inputPropertiesRepository.save(inputModel.getDescription());
                        itemData.setDescription(inputModel.getDescription());
                    }
                    if(inputModel.getName() != null) {
                        if(isClone){
                            inputModel.getName().setId(null);
                        }
                        inputPropertiesRepository.save(inputModel.getName());
                        itemData.setName(inputModel.getName());
                    }

                    if(isClone){
                        itemData.setId(null);
                    }else {
                        itemData.setId(inputModel.getId());
                    }

                    itemData.setFirstImage(inputModel.getImage());
                    itemData.setSecondImage(inputModel.getImage2());
                    itemData.setThirdImage(inputModel.getImage3());
                    itemData.setLink(inputModel.getLink());
                    itemData.setPosition(inputModel.getPosition());
                    itemData.setTabIndex(inputModel.getIndex());
                    itemData.setItem(item);
                    itemData.setPDF(inputModel.isPDF());
                    itemDataRepository.save(itemData);
                    itemDatas.add(itemData);


                }
                item.setItemData(itemDatas);
                itemRepository.save(item);

                items.add(item);

            }

        }
        cardlet.setBusinesses(businesses);
        cardlet.setItems(items);
        if(update){
            if(cardletDTO.getRemoveItems() != null) {
                List<Long> itemsToRemove = cardletDTO.getRemoveItems();
                for (Long itemToRemove : itemsToRemove) {
                    itemDataRepository.delete(itemToRemove);

                }
            }
            if(cardletDTO.getRemoveTabs() != null){
                List<Long> tabsToRemove = cardletDTO.getRemoveTabs();
                for (Long tabToRemove : tabsToRemove) {
                    Item ite = itemRepository.findOne(tabToRemove);
                    Set<ItemData> itemDats = ite.getItemData();
                    for (ItemData itemDat : itemDats) {
                        if(itemDat.getDescription() != null)
                            inputPropertiesRepository.delete(itemDat.getDescription());
                        if(itemDat.getName() != null)
                            inputPropertiesRepository.delete(itemDat.getName());
                        itemDataRepository.delete(itemDat);
                    }
                    itemRepository.delete(tabToRemove);
                }
            }
            if(cardletDTO.getRemoveBusiness() != null){
                List<Long> businessesToRemove = cardletDTO.getRemoveBusiness();
                for (Long businesseToRemove : businessesToRemove) {
                    Business bus = businessRepository.findOne(businesseToRemove);
                    if(bus.getAddress() != null)
                        inputPropertiesRepository.delete(bus.getAddress());
                    if(bus.getCompany() != null)
                        inputPropertiesRepository.delete(bus.getCompany());
                    if(bus.getJob() != null)
                        inputPropertiesRepository.delete(bus.getJob());
                    if(bus.getPhone() != null)
                        inputPropertiesRepository.delete(bus.getPhone());
                    if(bus.getSite() != null)
                        inputPropertiesRepository.delete(bus.getSite());
                    if(bus.getUserEmail() != null)
                        inputPropertiesRepository.delete(bus.getUserEmail());
                    if(bus.getUserName() != null)
                        inputPropertiesRepository.delete(bus.getUserName());
                    businessRepository.delete(businesseToRemove);

                }
            }
        }

        cardletRepository.save(cardlet);

        return null;
    }


    }
