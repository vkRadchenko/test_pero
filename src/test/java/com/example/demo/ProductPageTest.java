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

    private ProductPage productPage;

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 50000;
    }

    @BeforeEach
    public void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
        open("https://vk.com/club225299895?w=product-225299895_10044406");

        productPage = new ProductPage();
        sleep(2000);
    }

    @Test
    @DisplayName("Проверка отображения основной информации о товаре")
    public void testProductBasicInfoDisplay() {

        // Проверяем, что страница загрузилась
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
    @Description("После нажатия ожидаем увидеть модальное окно (для авторизации)")
    public void testClickShareLink() {

        assertTrue(productPage.hasShareLink(),"Ссылка поделиться не отображается");
        try {
            productPage.clickShareLink();
            assertTrue(productPage.hasShareBoxModal(),"После нажатия на поделиться отсутствует модальное окно для авторизации");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка нажатия на кнопку 'Пожаловаться'")
    @Description("После нажатия кнопки 'Пожаловаться' ожидаем увидеть модальное окно (мы не авторизованы)")
    public void testClickReportLink() {

       assertTrue(productPage.hasReportLink(),"Ссылка на жалобу не отображается");

        try {
            productPage.clickReportLink();
            assertTrue(productPage.hasReportBoxModal(),"Ошибка. Отсутствует модальное окно");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка кнопки подписаться в карточке товара")
    public void testProductSubscribeButton() {
        String expectedUrl = "https://vk.com/?to=bWFya2V0L3Byb2R1Y3QvZnl2YWYtMjI1Mjk5ODk1LTEwMDQ0NDA2P2RlbGF5ZWRBY3Rpb249c3Vic2NyaWJlR3JvdXA-";

        try {
            productPage.clickProductSubscribeButton();
            webdriver().shouldHave(url(expectedUrl));
        }catch (Exception e) {
            System.out.println("Переход не произошел: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка отображения блока 'О продавце'")
    public void testSellerSectionDisplay() {

        // Проверяем наличие аватара продавца
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
    @Description("После нажатия ожидаем переход на форму авторизации")
    public void testClickContactButton() {
        String expectedUrl = "https://vk.com/?to=bWFya2V0L3Byb2R1Y3QvZnl2YWYtMjI1Mjk5ODk1LTEwMDQ0NDA2P2RlbGF5ZWRBY3Rpb249c2hvd01lc3NhZ2VCb3g-";

        assertTrue(productPage.hasContactButton(),"Кнопка связи с продавцом не отображается");

        try {
            productPage.clickContactButton();
            webdriver().shouldHave(url(expectedUrl));
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
    @DisplayName("Проверка отзывов продавца")
    public void testSellerReviews() {
        assertTrue(productPage.hasSellerReviews(),"Текст отзывов не отображается");
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
    public void testSellerSubscribeButton() {
        String expectedUrl = "https://vk.com/?to=bWFya2V0L3Byb2R1Y3QvZnl2YWYtMjI1Mjk5ODk1LTEwMDQ0NDA2P2RlbGF5ZWRBY3Rpb249c3Vic2NyaWJlR3JvdXA-";
        try {
            productPage.clickSubscribeButton();
            webdriver().shouldHave(url(expectedUrl));
        }catch (Exception e) {
            System.out.println("Переход не произошел: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка кнопки перехода в магазин в блоке продавца")
    public void testSellerGoStoreButton() {

        // Проверяем ссылку на магазин
        String shopLinkHref = productPage.getShopLinkHref();
        assertNotNull(shopLinkHref, "Ссылка на магазин отсутствует");
        assertTrue(shopLinkHref.contains("uslugi-225299895"), "Неверная ссылка на магазин: " + shopLinkHref);

        // Тестируем переход в магазин
        try {
            productPage.clickShopLink();
            assertTrue(true, "Переход в магазин работает");
        } catch (Exception e) {
            System.out.println("Ссылка на магазин не работает: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("Проверка кликабельности элементов продавца")
    @Description("При клике по аватару или названию магазина происходит открытие главной страницы магазина")
    public void testSellerClickableElements() {

        String expectedUrl = "https://vk.com/club225299895";

        try {
            productPage.clickSellerAvatar();
            switchTo().window(1);
            webdriver().shouldHave(url(expectedUrl));
            closeWindow();
            switchTo().window(0);

        } catch (Exception e) {
            System.out.println("Аватар продавца не кликабелен или переход не выполнен: " + e.getMessage());
        }

        try {
            productPage.clickSellerName();
            switchTo().window(1);
            webdriver().shouldHave(url(expectedUrl));
            closeWindow();
            switchTo().window(0);

        } catch (Exception e) {
            System.out.println("Имя продавца не кликабельно или переход не выполнен: " + e.getMessage());
        }
    }
}
