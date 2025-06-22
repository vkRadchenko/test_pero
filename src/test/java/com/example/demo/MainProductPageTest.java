package com.example.demo;

import com.codeborne.selenide.*;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

public class MainProductPageTest {

    private final String BASE_URL = "https://vk.com/club225299895";
    private final String REDIRECT_AUTH_URL = "https://vk.com/?to=Y2x1YjIyNTI5OTg5NQ--";
    private final String REVIEWS_URL = "https://vk.com/reviews-225299895";
    private final String PRODUCTS_PAGE_URL = "https://vk.com/market-225299895?screen=group";
    private final String PRODUCT_CARD_URL = "https://vk.com/market/product/test-225299895-10863893";
    private final String SERVICE_CARD_URL = "https://vk.com/market/product/fyvaf-225299895-10044406";

    private MainProductPage mainProductPage;

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
    }

    @BeforeEach
    public void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
        open(BASE_URL);

        mainProductPage = new MainProductPage();
    }

    @DisplayName("Проверка кнопки Подписаться")
    @Description("Ожидаем редирект на страницу авторизации в текущей вкладке")
    @Test
    public void testSubscribeButton(){

        mainProductPage.getSubscribeButton()
                .shouldBe(Condition.visible)
                .click();
        webdriver().shouldHave(url(REDIRECT_AUTH_URL));
    }

    @DisplayName("Проверка рэйтинга")
    @Description("Ожидаем редирект на страницу авторизации в текущей вкладке")
    @Test
    public void testRatingStarsLink(){

        mainProductPage.getRatingStarsLink()
                .shouldBe(Condition.visible)
                .click();
        webdriver().shouldHave(WebDriverConditions.title("ВКонтакте | Добро пожаловать"));
    }

    @DisplayName("Проверка отзывов")
    @Description("Ожидаем редирект на страницу отзывов в текущей вкладке")
    @Test
    public void testReviewsLink(){

        mainProductPage.getReviewsLink()
                .shouldBe(Condition.visible)
                .click();
        webdriver().shouldHave(url(REVIEWS_URL));
    }

    @DisplayName("Проверка перехода в корзину")
    @Description("Ожидаем редирект на страницу корзины в текущей вкладке")
    @Test
    public void testCartButton(){

        mainProductPage.getCartButton()
                .shouldBe(Condition.visible)
                .click();
        webdriver().shouldHave(WebDriverConditions.title("Test public for test | Корзина"));
    }

    @DisplayName("Проверка ссылки 'Подробная информация")
    @Description("Ожидаем открытия модального окна")
    @Test
    public void testMoreInfoLink(){

        mainProductPage.getMoreInfo()
                .shouldBe(Condition.visible)
                .click();

        mainProductPage.getBoxModal().shouldBe(Condition.visible);
    }

    @DisplayName("Проверка кнопки показать все")
    @Description("Ожидаем переход на страницу со всеми товарами или услугами в текущей вкладке")
    @Test
    public void testShowAllProductsButton(){

        mainProductPage.getShowAllProductsButton()
                .scrollIntoView(true)
                .shouldBe(Condition.visible)
                .click();

        webdriver().shouldHave(url(PRODUCTS_PAGE_URL));
    }

    @DisplayName("Проверка ативной вкладки 'Продукты' и наличия товаров")
    @Description("Ожидаем, что вкладка по умолчанию открыта и в ней есть товары")
    @Test
    public void testProductsTab() {

        mainProductPage.getProductsTab()
                        .shouldHave(Condition.attribute("aria-selected", "true"));

        mainProductPage.hasProducts().shouldBe(Condition.exist);
        mainProductPage.getItems().shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @DisplayName("Проверка вкладки 'Услуги' и наличия услуг")
    @Description("Ожидаем переключение на вклудку и отображения услуг")
    @Test
    public void testServicesTab() {

        mainProductPage.getServicesTab()
                .scrollIntoView("{block: 'center'}")
                .click();
        mainProductPage.getServicesTab()
                .shouldHave(Condition.attribute("aria-selected", "true"));

        mainProductPage.hasServices().shouldBe(Condition.exist);
        mainProductPage.getItems().shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @DisplayName("Проверка переключения между вкладками")
    @Test
    public void testTabSwitching() {
        tabSelected(mainProductPage.getProductsTab(), true);

        switchToTab(mainProductPage.getServicesTab());
        tabSelected(mainProductPage.getServicesTab(), true);
        tabSelected(mainProductPage.getProductsTab(), false);

        switchToTab(mainProductPage.getProductsTab());
        tabSelected(mainProductPage.getProductsTab(), true);
        tabSelected(mainProductPage.getServicesTab(), false);
    }

    @DisplayName("Проверка перехода в товар по клике на карточку Продуктов")
    @Description("Ожидаем переход в карточку товара в новой вкладке")
    @Test
    public void testClickProductCard() {
        mainProductPage.getProductCardImage()
                .scrollIntoView("{block: 'center'}")
                .shouldBe(Condition.visible)
                .click();

        sleep(500);
        switchTo().window(1);
        webdriver().shouldHave(url(PRODUCT_CARD_URL));
        closeWindow();
        switchTo().window(0);
    }

    @DisplayName("Проверка перехода в услугу по клике на карточку Услуг")
    @Description("Ожидаем переход в карточку услуг в новой вкладке")
    @Test
    public void testClickServiceCard() {
        switchToTab(mainProductPage.getServicesTab());

        mainProductPage.getServiceCardImage()
                .scrollIntoView("{block: 'center'}")
                .click();

        sleep(500);
        switchTo().window(1);
        webdriver().shouldHave(url(SERVICE_CARD_URL));
        closeWindow();
        switchTo().window(0);
    }

    private void switchToTab(SelenideElement tab) {
        tab.scrollIntoView("{block: 'center'}").click();
    }

    private void tabSelected(SelenideElement tab, boolean selected) {
        String expected = selected ? "true" : "false";
        tab.shouldHave(Condition.attribute("aria-selected", expected));
    }
}
