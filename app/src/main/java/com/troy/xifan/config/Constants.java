package com.troy.xifan.config;

/**
 * Created by chenlongfei on 2016/11/9.
 */

public class Constants {
    public static class FanFou {
        //Consumer key
        public static final String CONSUMER_KEY = "f44caedd9890e6c2dff609314544245a";
        //Consumer secret
        public static final String CONSUMER_SECRET = "376efb84d711a7e59a9792c1ab8ba1ef";
        //fanfou URL
        public static final String FANFOU_URL = "http://fanfou.com";
        //fanfou api URL
        public static final String FANFOU_API_URL = "http://api.fanfou.com";
        //Request token URL
        public static final String REQUEST_TOKEN_URL = "/oauth/request_token";
        //Access token URL
        public static final String ACCESS_TOKEN_URL = FANFOU_URL + "/oauth/access_token";
        //Authorize URL
        public static final String AUTHORIZE_URL = "/oauth/authorize";

        //******statuses******
        public static final String HOME_TIMELINE_URL = "/statuses/home_timeline.json";
        public static final String PUBLIC_TIMELIN_URL = "/statuses/public_timeline.json";
        public static final String DESTROY_STATUSES_URL = "/statuses/destroy.json";
        public static final String UPDATE_STATUSES_URL = "/statuses/update.json";
        public static final String MENTIONS_MSG_URL = "/statuses/mentions.json";
        public static final String USER_TIMELINE_URL = "/statuses/user_timeline.json";

        //******direct_messages******
        public static final String CONVERSATION_LIST_URL =
                "/direct_messages/conversation_list.json";
        public static final String CONVERSATION_URL = "/direct_messages/conversation.json";
        public static final String SEND_DIRECT_MESSAGE_URL = "/direct_messages/new.json";

        //******favorites******
        public static final String CREATE_FAVORITES_URL = "/favorites/create/{id}.json";
        public static final String DESTROY_FAVORITES_URL = "/favorites/destroy/{id}.json";
        public static final String FAVORITES_URL = "/favorites.json";

        //******users******
        public static final String USER_FRIENDS_URL = "/users/friends.json";
        public static final String USER_SHOW_URL = "/users/show.json";
        public static final String USER_FOLLOWERS_URL = "/users/followers.json";

        //******photo******
        public static final String PHOTOS_UPLOAD_URL = "/photos/upload.json";
        public static final String USER_PHOTOS_URL = "/photos/user_timeline.json";

        //******trends******
        public static final String TRENDS_LIST_URL = "/trends/list.json";

        //******search******
        public static final String SEARCH_PUBLIC_TIMELINE = "/search/public_timeline.json";
        public static final String SEARCH_USER = "/search/users.json";
        public static final String SEARCH_USER_TIMELINE = "/search/user_timeline.json";

        //******friendships******
        public static final String CREATE_FRIENDSHIP_URL = "/friendships/create.json";
        public static final String DESTROY_FRIENDSHIP_URL = "/friendships/destroy.json";
        public static final String IS_EXIST_FRIENDSHIP_URL = "/friendships/exists.json";

        //******account******
        public static final String ACCOUNT_NOTIFICATION = "/account/notification.json";

        public static final String OAUTH_VERSION_VALUE = "1.0";
        public static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
        public static final String X_AUTH_MODE_VALUE = "client_auth";

        public static String OAUTH_TOKEN;
        public static String OAUTH_TOKENSECRET;
        public static String USERNAME;
        public static String PASSWORD;
    }

    public static class HeaderName {
        public static final String AUTHORIZATION = "Authorization";
    }

    public static class HttpBody {
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    }

    public static class XAuth {
        public static final String X_AUTH_USERNAME = "x_auth_username";
        public static final String X_AUTH_PASSWORD = "x_auth_password";
        public static final String X_AUTH_MODE = "x_auth_mode";
        public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
        public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
        public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
        public static final String OAUTH_NONCE = "oauth_nonce";
        public static final String OAUTH_VERSION = "oauth_version";
        public static final String OAUTH_TOKEN = "oauth_token";
        public static final String OAUTH_SIGNATURE = "oauth_signature";
    }

    public static class HttpMethod {
        public static final String GET = "GET";
        public static final String POST = "POST";
    }

    public static class Router {
        public static final String SCHEME = "xifan://";

        public static final String LOGIN = "login";
        public static final String MAIN = "main";
        public static final String WRITE_STATUS = "write_status";
        public static final String STATUS_DETAIL = "status_detail";
        public static final String LARGE_IMAGE = "large_image";
        public static final String USER_LIST = "user_list";
        public static final String SEARCH = "search";
        public static final String TREND_STATUSES = "trend_statuses";
        public static final String CONVERSATION = "conversation";
        public static final String SETTINGS = "settings";
        public static final String PROFILE = "profile";
        public static final String BROWSER = "browser";
    }

    public static class RequestParams {
        public static final String FORMAT_HTML = "html";
        public static final String MODE_LITE = "lite";
    }

    public static class SharedPreferences {
        public static final String NAME = "xifan";
        public static final String KEY_USER_ID = "user_id";
    }

    public static class AVOSCloud {
        public static final String APP_ID = "hNoFuqFvKx5crJBz6mMAFwPL-gzGzoHsz";
        public static final String APP_KEY = "jfEvEXMydhcRkgzL4hWWj72L";
    }

    public static class Fir {
        public static final String FIR_TOKEN = "2def92fc28bb812a895538fa54ed38d3";
    }
}
