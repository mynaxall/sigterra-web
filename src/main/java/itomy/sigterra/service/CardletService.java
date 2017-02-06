package itomy.sigterra.service;

import itomy.sigterra.domain.*;
import itomy.sigterra.repository.*;
import itomy.sigterra.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alexander on 2/2/17.
 */

@Service
@Transactional
public class CardletService {

    private final Logger log = LoggerFactory.getLogger(CardletService.class);


    @Inject
    private CardletRepository cardletRepository;

    @Inject
    BusinessRepository businessRepository;

    @Inject
    private UserService userService;

    @Inject
    private InputPropertiesRepository inputPropertiesRepository;

    @Inject
    private ItemDataRepository itemDataRepository;

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private TabTypeRepository tabTypeRepository;


    public List<UserCardletDTO> userCardlets(){
        List<Cardlet> caedletList = cardletRepository.findByUserIsCurrentUser();
        List<UserCardletDTO> usetCardletDTOs = new ArrayList<>();
        for (Cardlet cardlet : caedletList) {
            UserCardletDTO userCardletDTO = new UserCardletDTO();
            userCardletDTO.setCardletName(cardlet.getName());
            userCardletDTO.setId(cardlet.getId());
            Set<Business> businesses = cardlet.getBusinesses();
            Set<Item> items = cardlet.getItems();
            List<CardletTab> tabs = new ArrayList<>();
            for (Business business : businesses) {
                CardletTab cardletTab = new CardletTab();
                cardletTab.setName(business.getName());
                cardletTab.setPosition(business.getPisition());
                cardletTab.setUserName(business.getUserName());
                cardletTab.setUserEmail(business.getUserEmail());
                cardletTab.setPhone(business.getPhone());
                cardletTab.setAddress(business.getAddress());
                cardletTab.setCompany(business.getCompany());
                cardletTab.setSite(business.getSite());
                cardletTab.setJob(business.getJob());

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
                cardletTabItem.setName(item.getName());
                cardletTabItem.setPosition(item.getPisition());
                List <ItemModel> itemModels = new ArrayList<>();
                Set<ItemData> itemDatas = item.getItemData();

                for (ItemData itemData : itemDatas) {
                    ItemModel itemModel = new ItemModel();
                    itemModel.setDescription(itemData.getDescription());
                    itemModel.setImage(itemData.getFirstImage());
                    itemModel.setImage2(itemData.getSecondImage());
                    itemModel.setImage3(itemData.getThirdImage());
                    itemModel.setName(itemData.getName());
                    itemModel.setIndex(itemData.getTabIndex());
                    itemModel.setPosition(itemData.getPosition());
                    itemModel.setLink(itemData.getLink());
                    itemModels.add(itemModel);

                }

                cardletTabItem.setItems(itemModels);

                CardletLayout cardletLayout = new CardletLayout();
                cardletLayout.setMainColor(item.getMainColor());
                cardletLayout.setSecondaryColor(item.getColor());
                cardletLayout.setTabId(item.getTabType().getId());
                cardletLayout.setUrl(item.getTabType().getPath());

                cardletTabItem.setLayout(cardletLayout);

                tabs.add(cardletTabItem);

            }
            userCardletDTO.setTabs(tabs);
            usetCardletDTOs.add(userCardletDTO);

        }

        log.info("usetCardletDTOs =======  "+ usetCardletDTOs);


        return usetCardletDTOs;
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




    public UserCardletDTO createCardlet(UserCardletDTO cardletDTO) {

        log.debug("REST request to get a page of Cardlets22");
        Cardlet cardlet = new Cardlet();
        cardlet.setName(cardletDTO.getCardletName());
        cardlet.setUser(userService.getUserWithAuthorities());
        Set<Business> businesses  = new HashSet<>();
        Set <Item> items = new HashSet<>();
        List<CardletTab> tabs = cardletDTO.getTabs();

        cardlet = cardletRepository.save(cardlet);

        for (CardletTab tab : tabs) {
            if(tab.getTabType().equals(1)) {
                Business business = new Business();
                business.setName(tab.getName());
                if (tab.getUserEmail() != null) {
                    inputPropertiesRepository.save(tab.getUserEmail());
                    business.setUserEmail(tab.getUserEmail());
                }
                business.setPisition(tab.getPosition());
                business.setMainColor(tab.getLayout().getMainColor());
                business.setColor(tab.getLayout().getSecondaryColor());
                business.setCardlet(cardlet);
                if (tab.getUserName() != null) {
                    inputPropertiesRepository.save(tab.getUserName());
                    business.setUserName(tab.getUserName());
                }
                if (tab.getPhone() != null) {
                    inputPropertiesRepository.save(tab.getPhone());
                    business.setPhone(tab.getPhone());
                }
                if (tab.getAddress() != null) {
                    inputPropertiesRepository.save(tab.getAddress());
                    business.setAddress(tab.getAddress());
                }
                if (tab.getAddress() != null) {
                    inputPropertiesRepository.save(tab.getCompany());
                    business.setCompany(tab.getCompany());
                }
                if (tab.getSite() != null){
                    inputPropertiesRepository.save(tab.getSite());
                    business.setSite(tab.getSite());
                }
                if (tab.getJob() != null) {
                    inputPropertiesRepository.save(tab.getJob());
                    business.setJob(tab.getJob());
                }
                if (tab.getSocialLinks() != null) {

                    business.setTwitter(tab.getSocialLinks().getTwitter());
                    business.setFacebook(tab.getSocialLinks().getFacebook());
                    business.setGoogle(tab.getSocialLinks().getGoogle());
                    business.setLinkedIn(tab.getSocialLinks().getLinkedin());
                }
                log.info("asdasda ====== "+tab.getLayout().getTabId());
                business.setTabType(tabTypeRepository.findOne(tab.getLayout().getTabId()));
                businesses.add(business);
                businessRepository.save(business);
            }else if(tab.getTabType().equals(2)){
                Item item = new Item();
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
                        inputPropertiesRepository.save(inputModel.getDescription());
                        itemData.setDescription(inputModel.getDescription());
                    }
                    if(inputModel.getName() != null) {
                        inputPropertiesRepository.save(inputModel.getName());
                        itemData.setName(inputModel.getName());
                    }
                    itemData.setSecondImage(inputModel.getImage2());
                    itemData.setThirdImage(inputModel.getImage3());
                    itemData.setLink(inputModel.getLink());
                    itemData.setPosition(inputModel.getPosition());
                    itemData.setTabIndex(inputModel.getIndex());
                    itemData.setItem(item);
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

        cardletRepository.save(cardlet);

        return null;
    }


    }
