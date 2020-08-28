package fr.eni.ms2isi9bg3.gfv.enums;

public enum CommonStatus {
    AVAILABLE("status.type.available"),
    ARCHIVED("status.type.archived");

    private String i18nKey;

    /**
     * @param i18nKey the i18n key
     */
    private CommonStatus(final String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * @return the i18nKey
     */
    public String getI18nKey() {
        return this.i18nKey;
    }
}
