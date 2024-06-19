package com.ward.ward_server.api.releaseInfo.entity;

import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
import com.ward.ward_server.global.Object.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ward.ward_server.global.Object.Constants.FORMAT;

@Entity
@Getter
@Table(name = "release_info")
@NoArgsConstructor
public class ReleaseInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draw_platform_id")
    private DrawPlatform drawPlatform;

    private String siteUrl;

    @Column(nullable = false)
    private LocalDateTime releaseDate;

    private LocalDateTime dueDate;

    private LocalDateTime presentationDate;

    private Integer releasePrice;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)")
    private CurrencyUnit currencyUnit;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)", nullable = false)
    private NotificationMethod notificationMethod;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)", nullable = false)
    private ReleaseMethod releaseMethod;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)", nullable = false)
    private DeliveryMethod deliveryMethod;

    @Builder
    public ReleaseInfo(long itemId, DrawPlatform drawPlatform, String siteUrl,
                       String releaseDate, String dueDate, String presentationDate, Integer releasePrice, CurrencyUnit currencyUnit,
                       NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        this.itemId = itemId;
        this.drawPlatform = drawPlatform;
        this.siteUrl = siteUrl;
        this.releaseDate = LocalDateTime.parse(releaseDate, FORMAT);
        this.dueDate = LocalDateTime.parse(dueDate, FORMAT);
        this.presentationDate = LocalDateTime.parse(presentationDate, FORMAT);
        this.releasePrice = releasePrice;
        this.currencyUnit = currencyUnit;
        this.notificationMethod = notificationMethod;
        this.releaseMethod = releaseMethod;
        this.deliveryMethod = deliveryMethod;
    }

    public void updateItemId(long itemId) {
        this.itemId = itemId;
    }

    public void updateDrawPlatform(DrawPlatform drawPlatform) {
        this.drawPlatform = drawPlatform;
    }

    public void updateSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void updateReleaseDate(String releaseDate) {
        this.releaseDate = LocalDateTime.parse(releaseDate, FORMAT);
    }

    public void updateDueDate(String dueDate) {
        this.dueDate = LocalDateTime.parse(dueDate, FORMAT);
    }

    public void updatePresentationDate(String presentationDate) {
        this.presentationDate = LocalDateTime.parse(presentationDate, FORMAT);
    }

    public void updateReleasePrice(Integer releasePrice) {
        this.releasePrice = releasePrice;
    }

    public void updateCurrencyUnit(CurrencyUnit currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public void updateNotificationMethod(NotificationMethod notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    public void updateReleaseMethod(ReleaseMethod releaseMethod) {
        this.releaseMethod = releaseMethod;
    }

    public void updateDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getReleaseFormatDate() {
        return releaseDate.format(FORMAT);
    }

    public String getDueFormatDate() {
        return dueDate.format(FORMAT);
    }

    public String getPresentationFormatDate() {
        return presentationDate.format(FORMAT);
    }

    public LocalDateTime getReleaseLocalDate() {
        return releaseDate;
    }

    public LocalDateTime getDueLocalDate() {
        return dueDate;
    }

    public LocalDateTime getPresentationLocalDate() {
        return presentationDate;
    }
}
