package fr.eni.ms2isi9bg3.gfv.enums;

public enum BookingStatus {
    CONFIRMED("status.type.confirmed"),
    REJECTED("status.type.rejected"),
    CANCELED("status.type.canceled"),
    VALIDATION_PENDING("status.type.pending");

    private String i18nKey;

    /**
     * @param i18nKey the i18n key
     */
    private BookingStatus(final String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * @return the i18nKey
     */
    public String getI18nKey() {
        return this.i18nKey;
    }
}
