package itomy.sigterra.service.dto;

import java.util.List;

/**
 * Created by alexander on 1/31/17.
 */
public class CardletItemTab extends CardletTab {
    private List<ItemModel> items;

    public CardletItemTab() {
        super();
    }

    public CardletItemTab(List<ItemModel> items) {
        this.items = items;
    }


    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CardletItemTab{" +
            "items=" + items +
            '}';
    }
}
