package com.example.demo;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class MainProductPage {
    private final SelenideElement subscribeButton = $("span[class='FlatButton__in']");
    private final SelenideElement ratingStarsLink = $("[data-testid='rating-layout-main']");
    private final SelenideElement reviewsLink = $("[data-testid='rating-layout-after']");
    private final SelenideElement cartButton = $("a[class='groups_menu_item groups_menu_item_market_cart']");
    private final  SelenideElement moreInfo = $("a[class='groups-redesigned-info-more']");
    private final SelenideElement boxModal = $("div[id='wk_content']");
    private final SelenideElement showAllProductsButton = $("[data-role='show-all']");

    private final SelenideElement productsTab = $("[data-tab='market'] a");
    private final SelenideElement servicesTab = $("[data-tab='services'] a");

    private final SelenideElement productCardImage = $("[data-testid='product_card_picture']");
    private final SelenideElement serviceCardImage = $("[data-tab='services'] [data-testid='product_card_picture']");

    public SelenideElement getSubscribeButton() {
        if(!subscribeButton.exists()){
            System.out.println("Кнопка подписаться отсутствует");
        }
        return subscribeButton;
    }

    public SelenideElement getRatingStarsLink() {
        if(!ratingStarsLink.exists()){
            System.out.println("Звезды рэйтинга отсутствуют");
        }
        return ratingStarsLink;
    }

    public SelenideElement getReviewsLink() {
        if(!reviewsLink.exists()){
            System.out.println("Отзывы отсутствуют");
        }
        return reviewsLink;
    }
    public SelenideElement getCartButton() {
        if(!cartButton.exists()){
            System.out.println("Корзина отсутствует");
        }
        return cartButton;
    }

    public SelenideElement getMoreInfo(){
        if(!moreInfo.exists()){
            System.out.println("Корзина отсутствует");
        }
        return moreInfo;
    }

    public SelenideElement getBoxModal(){
        return boxModal;
    }

    public SelenideElement getShowAllProductsButton(){
        return showAllProductsButton;
    }

    public SelenideElement getProductsTab(){
        return productsTab;
    }
    public SelenideElement getServicesTab(){
        return servicesTab;
    }

    public SelenideElement hasProducts(){
        return $(".MarketGroupSectionProducts");
    }

    public SelenideElement hasServices(){
        return $(".MarketGroupSectionServices");
    }

    public ElementsCollection getItems() {
        return $$(".MarketItemPreviewList__item");
    }

    public SelenideElement getProductCardImage(){
        return productCardImage;
    }
    public SelenideElement getServiceCardImage(){
        return serviceCardImage;
    }
}
