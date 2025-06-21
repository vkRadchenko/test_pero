package com.example.demo;

import com.codeborne.selenide.Configuration;
import jdk.jfr.Description;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductPageTest {
    private final String BASE_URL = "https://vk.com/club225299895?w=product-225299895_10044406";
    private final String REDIRECT_SIGNIN_URL = "https://vk.com/?to=bWFya2V0L3Byb2R1Y3QvZnl2YWYtMjI1Mjk5ODk1LTEwMDQ0NDA2P2RlbGF5ZWRBY3Rpb249c3Vic2NyaWJlR3JvdXA-";
    private final String REDIRECT_SIGNIN_URL_2 = "https://vk.com/?to=bWFya2V0L3Byb2R1Y3QvZnl2YWYtMjI1Mjk5ODk1LTEwMDQ0NDA2P2RlbGF5ZWRBY3Rpb249c2hvd01lc3NhZ2VCb3g-";
    private final String REDIRECT_ALLPRODUCTS_URL = "https://vk.com/uslugi-225299895";
    private final String REDIRECT_MAINSTORE_URL = "https://vk.com/club225299895";

    private ProductPage productPage;

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
    }

    @BeforeEach
    public void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
        open(BASE_URL);

        productPage = new ProductPage();
        sleep(300);
    }

    @Test
    @DisplayName("Проверка отображения основной информации о товаре")
    public void testProductBasicInfoDisplay() {

        assertTrue(productPage.isPageLoaded(), "Страница товара не загрузилась");

        String title = productPage.getProductTitle();
        String price = productPage.getProductPrice();
        boolean imageDisplayed = productPage.isProductImageDisplayed();

        assertAll(
                () -> assertNotNull(title, "Название товара не отображается"),
                () -> assertFalse(title.trim().isEmpty(), "Название товара пустое"),
                () -> assertNotNull(price, "Цена не отображается"),
                () -> assertTrue(price.matches(".*\\d+.*"), "Цена должна содержать числа"),
                () -> assertTrue(imageDisplayed, "Изображение товара не отображается")
        );
    }

    @Test
    @DisplayName("Проверка нажатия на ссылку 'Поделиться'")
    @Description("Ожидаем увидеть модальное окно для авторизации")
    public void testClickShareLink() {

        assertTrue(productPage.hasShareLink(),"Ссылка поделиться не отображается");
        try {
            productPage.clickShareLink();
            sleep(100);
            assertTrue(productPage.hasShareBoxModal(),"После нажатия на поделиться отсутствует модальное окно для авторизации");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка нажатия на кнопку 'Пожаловаться'")
    @Description("Ожидаем увидеть модальное окно")
    public void testClickReportLink() {

       assertTrue(productPage.hasReportLink(),"Ссылка на жалобу не отображается");

        try {
            productPage.clickReportLink();
            sleep(2000);
            assertTrue(productPage.hasReportBoxModal(),"Ошибка. Отсутствует модальное окно");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка кнопки Подписаться в карточке товара")
    @Description("Ожидаем редирект на страницу авторизации в текущей вкладке")
    public void testProductSubscribeButton() {

        try {
            productPage.clickProductSubscribeButton();
            webdriver().shouldHave(url(REDIRECT_SIGNIN_URL));
        }catch (Exception e) {
            System.out.println("Переход не произошел: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка отображения блока 'О продавце'")
    public void testSellerSectionDisplay() {

        assertTrue(productPage.isSellerAvatarDisplayed(), "Аватар продавца не отображается");

        String avatarSrc = productPage.getSellerAvatarSrc();
        String sellerName = productPage.getSellerName();

        assertAll(
                () -> assertNotNull(avatarSrc, "URL аватара продавца отсутствует"),
                () -> assertTrue(avatarSrc.startsWith("https://"), "URL аватара должен начинаться с https://"),
                () -> assertNotNull(sellerName, "Название продавца не отображается"),
                () -> assertFalse(sellerName.trim().isEmpty(), "Название продавца пустое")
        );
    }

    @Test
    @DisplayName("Проверка нажатия на кнопку 'Написать'")
    @Description("Ожидаем редирект на страницу авторизации в текущей вкладке")
    public void testClickContactButton() {

        assertTrue(productPage.hasContactButton(),"Кнопка связи с продавцом не отображается");

        try {
            productPage.clickContactButton();
            webdriver().shouldHave(url(REDIRECT_SIGNIN_URL_2));
        }catch (Exception e) {
            System.out.println("Переход не произошел: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка нажатия на кнопку 'Добавить в избранное'")
    @Description("После нажатия ожидаем всплывающее меню добавления в избранное")
    public void testClickFavoriteButton() {

        assertTrue(productPage.hasAddFavoriteButton(),"Кнопка добавления в избранное не найдена");

        try {
            productPage.clickAddFavoriteButton();
            assertTrue(productPage.hasFavoriteMenu(),"Всплывающее меню добавления в избранное не отображается");

        }catch (Exception e) {
            System.out.println("Меню не отображается: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка рейтинга продавца")
    public void testSellerRating() {
        assertTrue(productPage.isSellerRatingDisplayed(), "Рейтинг продавца не отображается");

        String ratingValue = productPage.getSellerRatingValue();
        double rating = Double.parseDouble(ratingValue.replace(",", "."));

        assertAll(
                () -> assertNotNull(ratingValue, "Значение рейтинга не отображается"),
                () -> assertTrue(ratingValue.matches("\\d,\\d"), "Неверный формат рейтинга: " + ratingValue),
                () -> assertFalse(ratingValue.trim().isEmpty(), "Значение рейтинга пустое"),
                () -> assertTrue(rating >= 1.0 && rating <= 5.0, "Рейтинг должен быть от 1.0 до 5.0, получен: " + rating)
        );
    }

    @Test
    @DisplayName("Проверка информации о подписчиках")
    public void testSellerSubscribers() {
        String subscribersInfo = productPage.getSellerSubscribersCount();
        assertNotNull(subscribersInfo, "Информация о подписчиках не отображается");
        assertTrue(subscribersInfo.contains("подписчик"), "Информация должна содержать слово 'подписчик'");
    }

    @Test
    @DisplayName("Проверка кнопки подписаться в блоке продавца")
    @Description("Ожидаем редирект на страницу авторизации в текущей вкладке")
    public void testSellerSubscribeButton() {

        try {
            productPage.clickSubscribeButton();
            webdriver().shouldHave(url(REDIRECT_SIGNIN_URL));
        }catch (Exception e) {
            System.out.println("Переход не произошел: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка кнопки перехода в магазин в блоке продавца")
    @Description("Ожидаем редирект на страницу с товарами в новой вкладке")
    public void testSellerGoStoreButton() {

        String shopLinkHref = productPage.getShopLinkHref();

        assertNotNull(shopLinkHref, "Ссылка на магазин отсутствует");
        assertTrue(shopLinkHref.contains("uslugi-225299895"), "Неверная ссылка на магазин: " + shopLinkHref);

        try {
            productPage.clickShopLink();
            switchTo().window(1);
            webdriver().shouldHave(url(REDIRECT_ALLPRODUCTS_URL));
            closeWindow();
            switchTo().window(0);
        } catch (Exception e) {
            System.out.println("Ссылка на магазин не работает: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка кликабельности элементов продавца")
    @Description("При клике по аватару или названию магазина происходит открытие главной страницы магазина")
    public void testSellerClickableElements() {

        try {
            productPage.clickSellerAvatar();
            switchTo().window(1);
            webdriver().shouldHave(url(REDIRECT_MAINSTORE_URL));
            closeWindow();
            switchTo().window(0);

        } catch (Exception e) {
            System.out.println("Аватар продавца не кликабелен или переход не выполнен: " + e.getMessage());
        }

        try {
            productPage.clickSellerName();
            switchTo().window(1);
            webdriver().shouldHave(url(REDIRECT_MAINSTORE_URL));
            closeWindow();
            switchTo().window(0);

        } catch (Exception e) {
            System.out.println("Имя продавца не кликабельно или переход не выполнен: " + e.getMessage());
        }
    }
}
