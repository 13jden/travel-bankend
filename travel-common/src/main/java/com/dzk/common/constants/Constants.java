package com.dzk.common.constants;

public class Constants {

    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{6,18}$";

    public static final Integer REDIS_KEY_EXPIRES_TIME = 60000;

    public static String REDIS_KEY_PRE = "Idouso:";

    public static String REDIS_KEY_CHECK_CODE =REDIS_KEY_PRE + "checkcode:";

    public static final String REDIS_KEY_ACTIVE_MESSAGE = REDIS_KEY_PRE + "userActive:message:";

    public static final Integer LENGTH_10 = 10;

    public static final Integer REDIS_KEY_EXPIRES_ONE_DAY = 60000*60*24;
    public static final Integer REDIS_KEY_EXPIRES_SEVEN_DAYS = 60000*60*24*7;

    public static final String REDIS_KEY_TOKEN_WEB = REDIS_KEY_PRE + "token:web:";

    public static final String TOKEN_WEB =  "token";

    public static final String TOKEN_ADMIN =  "adminToken";

    public static final String REDIS_KEY_TOKEN_ADMIN = REDIS_KEY_PRE + "token:admin";

    /**
     * Redis 分类列表 key
     */
    public static final String REDIS_KEY_CATEGORY_LIST = "category:list";

    /**
     * Redis Banner列表 key
     */
    public static final String REDIS_KEY_BANNER_LIST = "banner:list";


    /**
     * Redis 用户租借历史 key前缀
     */
    public static final String REDIS_KEY_USER_RENTAL_HISTORY = REDIS_KEY_PRE + "user:rental:";

    public static final String MESSAGE_QUEUE_KEY = "message:queue";

    public static final String REDIS_KEY_UPLOAD_VIDEO = REDIS_KEY_PRE +"upload:video:";

    public static final String REDIS_KEY_PRE_UPLOAD_VIDEO = REDIS_KEY_PRE +"pre:upload:video:";

    public static final String REDIS_KEY_UPLOAD_VIDEO_INFO = REDIS_KEY_PRE +"upload:video:info:";

    public static final String MESSAGE_TYPE_USER_2_USER = "USER_2_USER";

    public static final String MESSAGE_TYPE_ADMIN_2_USER = "ADMIN_2_USER";

    public static final String MESSAGE_TYPE_NOTIFICATION_2_USER = "NOTIFICATION_2_USER";

    public static final String MESSAGE_TYPE_SYSTEM_2_USER = "SYSTEM_2_USER";

    /**
     * Redis 视频排行榜 ZSET key
     */
    public static final String REDIS_KEY_VIDEO_RANKING_ZSET = REDIS_KEY_PRE + "video:ranking:zset";

    /**
     * Redis 用户观看历史记录 key 前缀
     */
    public static final String REDIS_KEY_USER_VIEW_HISTORY = REDIS_KEY_PRE + "user:view:history:";

    /**
     * 排行榜分数
     */
    public static final double SCORE_VIEW = 1.0;
    public static final double SCORE_LIKE = 3.0;
    public static final double SCORE_COMMENT = 3.0;
    public static final double SCORE_FAVORITE = 2.0;
    public static final double SCORE_SHARE = 5.0;
    public static final double SCORE_SEARCH_HIT = 1.0;

    /**
     * 排行榜 ZSET 的过期时间 (3天)
     */
    public static final int REDIS_KEY_EXPIRES_THREE_DAYS = 60 * 60 * 24 * 3;

    /**
     * 用户观看历史记录的最大长度
     */
    public static final int USER_VIEW_HISTORY_MAX_SIZE = 100;

    /**
     * Redis 稿件浏览量 HASH key
     */
    public static final String REDIS_KEY_MANUSCRIPT_VIEWS = REDIS_KEY_PRE + "manuscript:views";

    /**
     * Redis 热门视频缓存 key
     */
    public static final String REDIS_KEY_TOP_10_VIDEOS = REDIS_KEY_PRE + "video:top10:";

}


