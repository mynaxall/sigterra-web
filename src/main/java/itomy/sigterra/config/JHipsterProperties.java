package itomy.sigterra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * Properties specific to JHipster.
 * <p>
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "jhipster", ignoreUnknownFields = false)
public class JHipsterProperties {

    private final Async async = new Async();

    private final Http http = new Http();

    private final Cache cache = new Cache();

    private final Mail mail = new Mail();

    private final Security security = new Security();

    private final Swagger swagger = new Swagger();

    private final Metrics metrics = new Metrics();

    private final CorsConfiguration cors = new CorsConfiguration();

    private final Ribbon ribbon = new Ribbon();

    private final AWSS3Bucket awss3Bucket = new AWSS3Bucket();

    private final GeoData geoData = new GeoData();

    private final EventWorker eventWorker = new EventWorker();

    private final SigterraProperties sigterraProperties = new SigterraProperties();

    public Async getAsync() {
        return async;
    }

    public Http getHttp() {
        return http;
    }

    public Cache getCache() {
        return cache;
    }

    public Mail getMail() {
        return mail;
    }

    public Security getSecurity() {
        return security;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public CorsConfiguration getCors() {
        return cors;
    }

    public Ribbon getRibbon() {
        return ribbon;
    }

    public SigterraProperties getSigterraProperties() {
        return sigterraProperties;
    }


    public AWSS3Bucket getAwss3Bucket() {
        return awss3Bucket;
    }

    public GeoData getGeoData() {
        return geoData;
    }

    public EventWorker getEventWorker() {
        return eventWorker;
    }

    public static class Async {

        private int corePoolSize = 2;

        private int maxPoolSize = 50;

        private int queueCapacity = 10000;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class Http {

        private final Cache cache = new Cache();

        public Cache getCache() {
            return cache;
        }

        public static class Cache {

            private int timeToLiveInDays = 1461;

            public int getTimeToLiveInDays() {
                return timeToLiveInDays;
            }

            public void setTimeToLiveInDays(int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }
    }

    public static class Cache {
    }

    public static class Mail {

        private String from = "sigterra_web@localhost";

        private String baseUrl = "";

        private String label = "Sigterra Support Team";

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public static class Security {

        private final RememberMe rememberMe = new RememberMe();

        public RememberMe getRememberMe() {
            return rememberMe;
        }

        public static class RememberMe {

            @NotNull
            private String key;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }

    public static class Swagger {

        private String title = "sigterra_web API";

        private String description = "sigterra_web API documentation";

        private String version = "0.0.1";

        private String termsOfServiceUrl;

        private String contactName;

        private String contactUrl;

        private String contactEmail;

        private String license;

        private String licenseUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }
    }

    public Logging getLogging() {
        return logging;
    }

    private final Logging logging = new Logging();

    public static class Metrics {

        private final Jmx jmx = new Jmx();

        private final Graphite graphite = new Graphite();

        private final Logs logs = new Logs();

        public Jmx getJmx() {
            return jmx;
        }

        public Graphite getGraphite() {
            return graphite;
        }

        public Logs getLogs() {
            return logs;
        }

        public static class Jmx {

            private boolean enabled = true;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class Graphite {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 2003;

            private String prefix = "sigterra_web";

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getPrefix() {
                return prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }
        }

        public static class Logs {

            private boolean enabled = false;

            private long reportFrequency = 60;

            public long getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(int reportFrequency) {
                this.reportFrequency = reportFrequency;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    public static class Logging {

        private final Logstash logstash = new Logstash();

        public Logstash getLogstash() {
            return logstash;
        }

        public static class Logstash {

            private boolean enabled = false;

            private String host = "localhost";

            private int port = 5000;

            private int queueSize = 512;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getQueueSize() {
                return queueSize;
            }

            public void setQueueSize(int queueSize) {
                this.queueSize = queueSize;
            }
        }
    }

    public static class Ribbon {

        private String[] displayOnActiveProfiles;

        public String[] getDisplayOnActiveProfiles() {
            return displayOnActiveProfiles;
        }

        public void setDisplayOnActiveProfiles(String[] displayOnActiveProfiles) {
            this.displayOnActiveProfiles = displayOnActiveProfiles;
        }
    }

    public static class AWSS3Bucket {
        private String name;
        private String credentials;
        private String region;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCredentials() {
            return credentials;
        }

        public void setCredentials(String credentials) {
            this.credentials = credentials;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }

    public static class GeoData {
        private Integer cacheExpirationTimeout;
        private TimeUnit cacheExpirationTimeUnit;
        private Integer cleanCacheDelay;
        private TimeUnit cleanCacheTimeUnit;
        private Integer resolveLocationTimeout;
        private TimeUnit resolveLocationTimeUnit;

        public Integer getCacheExpirationTimeout() {
            return cacheExpirationTimeout;
        }

        public void setCacheExpirationTimeout(Integer cacheExpirationTimeout) {
            this.cacheExpirationTimeout = cacheExpirationTimeout;
        }

        public TimeUnit getCacheExpirationTimeUnit() {
            return cacheExpirationTimeUnit;
        }

        public void setCacheExpirationTimeUnit(TimeUnit cacheExpirationTimeUnit) {
            this.cacheExpirationTimeUnit = cacheExpirationTimeUnit;
        }

        public Integer getCleanCacheDelay() {
            return cleanCacheDelay;
        }

        public void setCleanCacheDelay(Integer cleanCacheDelay) {
            this.cleanCacheDelay = cleanCacheDelay;
        }

        public TimeUnit getCleanCacheTimeUnit() {
            return cleanCacheTimeUnit;
        }

        public void setCleanCacheTimeUnit(TimeUnit cleanCacheTimeUnit) {
            this.cleanCacheTimeUnit = cleanCacheTimeUnit;
        }

        public Integer getResolveLocationTimeout() {
            return resolveLocationTimeout;
        }

        public void setResolveLocationTimeout(Integer resolveLocationTimeout) {
            this.resolveLocationTimeout = resolveLocationTimeout;
        }

        public TimeUnit getResolveLocationTimeUnit() {
            return resolveLocationTimeUnit;
        }

        public void setResolveLocationTimeUnit(TimeUnit resolveLocationTimeUnit) {
            this.resolveLocationTimeUnit = resolveLocationTimeUnit;
        }
    }

    public static class EventWorker {
        private Integer processEventDelay;
        private Integer processEventInitialDelay;
        private TimeUnit processEventDelayTimeUnit;

        public Integer getProcessEventDelay() {
            return processEventDelay;
        }

        public void setProcessEventDelay(Integer processEventDelay) {
            this.processEventDelay = processEventDelay;
        }

        public Integer getProcessEventInitialDelay() {
            return processEventInitialDelay;
        }

        public void setProcessEventInitialDelay(Integer processEventInitialDelay) {
            this.processEventInitialDelay = processEventInitialDelay;
        }

        public TimeUnit getProcessEventDelayTimeUnit() {
            return processEventDelayTimeUnit;
        }

        public void setProcessEventDelayTimeUnit(TimeUnit processEventDelayTimeUnit) {
            this.processEventDelayTimeUnit = processEventDelayTimeUnit;
        }
    }

    public static class SigterraProperties {
        private String pathBannerImages;
        private String pathLinkImages;

        public String getPathBackgroundImages() {
            return pathBannerImages;
        }

        public void setPathBannerImages(String pathBannerImages) {
            this.pathBannerImages = pathBannerImages;
        }

        public String getPathLinkImages() {
            return pathLinkImages;
        }

        public void setPathLinkImages(String pathLinkImages) {
            this.pathLinkImages = pathLinkImages;
        }
    }
}
