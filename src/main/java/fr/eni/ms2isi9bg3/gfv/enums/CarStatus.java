package fr.eni.ms2isi9bg3.gfv.enums;

public enum CarStatus {
    AVAILABLE("status.type.available"),
    RESERVED("status.type.reserved"),
    UNDER_MAINTENANCE("status.type.underMaintenance"),
    ARCHIVED("status.type.archived");

    private String i18nKey;

    /**
     * @param i18nKey the i18n key
     */
    private CarStatus(final String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * @return the i18nKey
     */
    public String getI18nKey() {
        return this.i18nKey;
    }
}
