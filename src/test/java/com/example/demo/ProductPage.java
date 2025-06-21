package com.example.demo;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

// page_url = https://vk.com/club225299895?w=product-225299895_10044406
public class ProductPage {

    private final SelenideElement productTitle = $("[data-testid='market_item_page_title']");
    private final SelenideElement productPrice = $("[data-testid='market_item_page_price']");
    private final SelenideElement productImage = $("[data-testid='item_page_main_photo']");
    private final SelenideElement productReportLink = $("[data-testid='market_item_page_report']");
    private final SelenideElement productBoxModal = $("[data-testid='box_layout']");
    private final SelenideElement productShareLink = $("[data-testid='market_item_page_share']");
    private final SelenideElement productContactButton = $("[data-testid='market_item_page_primary_button']");
    private final SelenideElement productAddFavoriteButton = $("[data-testid='market_item_page_actions_opener_button_not_checked']");
    private final SelenideElement productFavoriteMenu = $("[data-testid='dropdownactionsheet']");
    private final SelenideElement productSubscribeButton = $("[data-testid='market_item_page_subscribe_button']");


    // Блок о продавце
    private final SelenideElement sellerAvatar = $("[data-testid='market_item_page_group_avatar']");
    private final SelenideElement sellerName = $("[data-testid='market_item_page_shop_text']");
    private final SelenideElement sellerRating = $("[data-testid='market_item_page_rating']");
    private final SelenideElement sellerRatingValue = $("[data-testid='rating-layout-indicator']");
    private final SelenideElement sellerSubscribers = $("[data-testid='market_item_page_friends']");
    private final SelenideElement subscribeButton = $("[data-testid='market_item_page_group_subscribe']");
    private final SelenideElement shopLink = $("[data-testid='market_item_page_shop_link']");

    // Методы для взаимодействия с элементами
    public String getProductTitle() {
        return productTitle.getText();
    }

    public String getProductPrice() {
        return productPrice.getText();
    }

    public boolean hasReportLink() {
        return productReportLink.exists();
    }

    public void clickReportLink() {
        try {
            scrollElement(productReportLink);
             productReportLink.click();
        }catch (Exception e){
            throw new RuntimeException("Не удалось открыть окно жалобы: " + e.getMessage());
        }
    }

    public boolean hasReportBoxModal() {
        return productBoxModal.exists();
    }

    public boolean isProductImageDisplayed() {
        return productImage.isDisplayed();
    }

    public boolean hasShareLink() {
        return productShareLink.exists();
    }

    public boolean hasShareBoxModal() {
        return productBoxModal.exists();
    }

    public void clickShareLink() {
        try {
            productShareLink.click();
        }catch (Exception e){
            throw new RuntimeException("Не удалось открыть окно поделиться: " + e.getMessage());
        }
    }

    public boolean hasContactButton() {
        return productContactButton.exists();
    }

    public void clickContactButton() {
        try {
            productContactButton.click();
        }catch (Exception e){
            throw new RuntimeException("Не удалось открыть окно связи с продавцом: " + e.getMessage());
        }
    }

    public boolean hasAddFavoriteButton() {
        return productAddFavoriteButton.exists();
    }

    public void clickAddFavoriteButton() {
        try {
            productAddFavoriteButton.click();
        }catch (Exception e){
            throw new RuntimeException("Не удалось открыть подменю добавления в избранное: " + e.getMessage());
        }
    }

    public boolean hasFavoriteMenu() {
        return productFavoriteMenu.exists();
    }

    public void clickProductSubscribeButton() {
        productSubscribeButton.click();
    }

    // Методы для работы с блоком "О продавце"

    public boolean isSellerAvatarDisplayed() {
        return sellerAvatar.exists() && sellerAvatar.isDisplayed();
    }

    public String getSellerName() {
        return sellerName.exists() ? sellerName.getText() : "";
    }

    public String getSellerAvatarSrc() {
        return sellerAvatar.exists() ? sellerAvatar.$("img").getAttribute("src") : "";
    }

    public boolean isSellerRatingDisplayed() {
        return sellerRating.exists() && sellerRating.isDisplayed();
    }

    public String getSellerRatingValue() {
        return sellerRatingValue.exists() ? sellerRatingValue.getText() : "";
    }

    public String getSellerSubscribersCount() {
        return sellerSubscribers.exists() ? sellerSubscribers.getText() : "";
    }

    public void clickSubscribeButton() {
        if (subscribeButton.exists() && subscribeButton.isDisplayed()) {
            scrollElement(subscribeButton);
            subscribeButton.click();
        } else {
            throw new RuntimeException("Кнопка 'Подписаться' не найдена или не отображается");
        }
    }

    public void clickShopLink() {
        if (shopLink.exists()) {
            scrollElement(shopLink);
            shopLink.click();
        }
    }

    public String getShopLinkHref() {
        return shopLink.exists() ? shopLink.getAttribute("href") : "";
    }

    public void clickSellerAvatar() {
        if (sellerAvatar.exists()) {
            scrollElement(sellerAvatar);
            sellerAvatar.click();
        }
    }

    public void clickSellerName() {
        if (sellerName.exists()) {
            sellerName.click();
        }
    }

    public boolean isPageLoaded() {
        return productTitle.exists() || productPrice.exists();
    }

    private void scrollElement(SelenideElement locator){
        locator.scrollIntoView(true).shouldBe(Condition.visible,Condition.enabled);
    }
}
