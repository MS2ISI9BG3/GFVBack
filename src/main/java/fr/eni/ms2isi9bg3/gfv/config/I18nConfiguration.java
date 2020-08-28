package fr.eni.ms2isi9bg3.gfv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * Configuration class for i18n files
 */
@Configuration
public class I18nConfiguration {

    /**
     * @return Spring message source bean
     */
    @Bean
    @Description("Spring message resolver")
    public ReloadableResourceBundleMessageSource messageSource() {
        Locale locale = Locale.FRENCH;
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultLocale(locale);
        return messageSource;
    }

}
