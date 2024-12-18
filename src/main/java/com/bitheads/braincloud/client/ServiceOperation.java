package com.bitheads.braincloud.client;

/**
 * Created by prestonjennings on 15-09-01.
 */
public enum ServiceOperation {
    AUTHENTICATE,
    RESET_EMAIL_PASSWORD,
    RESET_EMAIL_PASSWORD_ADVANCED,
    RESET_EMAIL_PASSWORD_WITH_EXPIRY,
    RESET_EMAIL_PASSWORD_ADVANCED_WITH_EXPIRY,
    RESET_UNIVERSAL_ID_PASSWORD,
    RESET_UNIVERSAL_ID_PASSWORD_ADVANCED,
    RESET_UNIVERSAL_ID_PASSWORD_WITH_EXPIRY,
    RESET_UNIVERSAL_ID_PASSWORD_ADVANCED_WITH_EXPIRY,

    // Async Match
    ABANDON,
    ABANDON_MATCH_WITH_SUMMARY_DATA,
    COMPLETE,
    COMPLETE_MATCH_WITH_SUMMARY_DATA,
    CREATE,
    DELETE_MATCH,
    FIND_MATCHES,
    FIND_MATCHES_COMPLETED,
    READ_MATCH,
    READ_MATCH_HISTORY,
    SUBMIT_TURN,
    UPDATE_MATCH_STATE_CURRENT_TURN,
    UPDATE_SUMMARY,

    // Event
    DELETE_INCOMING,
    DELETE_INCOMING_EVENTS,
    DELETE_INCOMING_EVENTS_BY_TYPE_OLDER_THAN,
    DELETE_INCOMING_EVENTS_OLDER_THAN,
    GET_EVENTS,
    SEND,
    UPDATE_EVENT_DATA,
    UPDATE_EVENT_DATA_IF_EXISTS,

    // Group
    ACCEPT_GROUP_INVITATION,
    ADD_GROUP_MEMBER,
    APPROVE_GROUP_JOIN_REQUEST,
    AUTO_JOIN_GROUP,
    AUTO_JOIN_GROUP_MULTI,
    CANCEL_GROUP_INVITATION,
    CREATE_GROUP,
    CREATE_GROUP_ENTITY,
    DELETE_GROUP,
    DELETE_GROUP_ENTITY,
    DELETE_GROUP_JOIN_REQUEST,
    GET_MY_GROUPS,
    GET_RANDOM_GROUPS_MATCHING,
    INCREMENT_GROUP_DATA,
    INCREMENT_GROUP_ENTITY_DATA,
    INVITE_GROUP_MEMBER,
    JOIN_GROUP,
    LEAVE_GROUP,
    LIST_GROUPS_PAGE,
    LIST_GROUPS_PAGE_BY_OFFSET,
    LIST_GROUPS_WITH_MEMBER,
    READ_GROUP,
    READ_GROUP_DATA,
    READ_GROUP_ENTITIES_PAGE,
    READ_GROUP_ENTITIES_PAGE_BY_OFFSET,
    READ_GROUP_ENTITY,
    READ_GROUP_MEMBERS,
    REJECT_GROUP_INVITATION,
    REJECT_GROUP_JOIN_REQUEST,
    REMOVE_GROUP_MEMBER,
    SET_GROUP_OPEN,
    UPDATE_GROUP_ACL,
    UPDATE_GROUP_DATA,
    UPDATE_GROUP_ENTITY_ACL,
    UPDATE_GROUP_ENTITY_DATA,
    UPDATE_GROUP_MEMBER,
    UPDATE_GROUP_NAME,
    UPDATE_GROUP_SUMMARY_DATA,

    // Identity
    ATTACH,
    ATTACH_BLOCKCHAIN_IDENTITY,
    ATTACH_NONLOGIN_UNIVERSAL,
    ATTACH_PARENT_WITH_IDENTITY,
    ATTACH_PEER_PROFILE,
    CHANGE_EMAIL_IDENTITY,
    DETACH,
    DETACH_BLOCKCHAIN_IDENTITY,
    DETACH_PARENT,
    DETACH_PEER,
    GET_CHILD_PROFILES,
    GET_EXPIRED_IDENTITIES,
    GET_IDENTITIES,
    GET_IDENTITY_STATUS,
    GET_PEER_PROFILES,
    MERGE,
    REFRESH_IDENTITY,
    SWITCH_TO_CHILD_PROFILE,
    SWITCH_TO_PARENT_PROFILE,
    UPDATE_UNIVERSAL_LOGIN,

    //
    DELETE,
    READ,
    READ_SINGLETON,
    INCREMENT_SINGLETON_DATA,
    READ_FRIENDS,
    READ_SHARED,
    READ_SHARED_ENTITY,
    UPDATE,
    RESET,
    VERIFY,
    READ_ACHIEVEMENTS,
    AWARD_ACHIEVEMENTS,
    READ_ACHIEVED_ACHIEVEMENTS,
    UPDATE_REWARDS,
    READ_QUESTS,
    READ_COMPLETED_QUESTS,
    READ_IN_PROGRESS_QUESTS,
    READ_NOT_STARTED_QUESTS,
    READ_QUESTS_WITH_STATUS,
    READ_QUESTS_WITH_BASIC_PERCENTAGE,
    READ_QUESTS_WITH_COMPLEX_PERCENTAGE,
    READ_QUESTS_BY_CATEGORY,
    RESET_MILESTONES,

    READ_FOR_CATEGORY,

    READ_MILESTONES,
    READ_MILESTONES_BY_CATEGORY,
    READ_COMPLETED_MILESTONES,
    READ_IN_PROGRESS_MILESTONES,

    PROCESS_STATISTICS,
    UPDATE_INCREMENT,
    READ_NEXT_XPLEVEL,
    SET_XPPOINTS,
    READ_SUBSET,
    READ_XP_LEVELS,

    PING_DATA,
    GET_LIST,
    GET_LIST_BY_INDEXED_ID,
    GET_LIST_COUNT,
    GET_PAGE,
    GET_ENTITY_PAGE,
    GET_PAGE_BY_OFFSET,
    GET_ENTITY_PAGE_OFFSET,
    INCREMENT_USER_ENTITY_DATA,
    INCREMENT_SHARED_USER_ENTITY_DATA,
    INCREMENT_GLOBAL_ENTITY_DATA,
    GET_RANDOM_ENTITIES_MATCHING,
    READ_SHARED_ENTITIES_LIST,

    UPDATE_ACL,
    UPDATE_PARTIAL,
    UPDATE_SHARED,
    UPDATE_SET_MINIMUM,
    UPDATE_INCREMENT_TO_MAXIMUM,

    // Leaderboard
    GET_GLOBAL_LEADERBOARD_ENTRY_COUNT,
    GET_GLOBAL_LEADERBOARD_PAGE,
    GET_GLOBAL_LEADERBOARD_PAGE_IF_EXISTS,
    GET_GLOBAL_LEADERBOARD_VERSIONS,
    GET_GLOBAL_LEADERBOARD_VIEW,
    GET_GLOBAL_LEADERBOARD_VIEW_IF_EXISTS,
    GET_GROUP_LEADERBOARD_VIEW,
    GET_GROUP_SOCIAL_LEADERBOARD,
    GET_GROUP_SOCIAL_LEADERBOARD_BY_VERSION,
    GET_MULTI_SOCIAL_LEADERBOARD,
    GET_PLAYER_SCORE,
    GET_PLAYER_SCORES,
    GET_PLAYER_SCORES_FROM_LEADERBOARDS,
    GET_PLAYERS_SOCIAL_LEADERBOARD,
    GET_PLAYERS_SOCIAL_LEADERBOARD_BY_VERSION,
    GET_PLAYERS_SOCIAL_LEADERBOARD_BY_VERSION_IF_EXISTS,
    GET_PLAYERS_SOCIAL_LEADERBOARD_IF_EXISTS,
    GET_SOCIAL_LEADERBOARD,
    GET_SOCIAL_LEADERBOARD_BY_VERSION,
    GET_SOCIAL_LEADERBOARD_BY_VERSION_IF_EXISTS,
    GET_SOCIAL_LEADERBOARD_IF_EXISTS,
    LIST_ALL_LEADERBOARDS,
    POST_GROUP_SCORE,
    POST_SCORE,
    POST_SCORE_DYNAMIC,
    POST_SCORE_DYNAMIC_USING_CONFIG,
    REMOVE_GROUP_SCORE,
    REMOVE_PLAYER_SCORE,

    //
    GET_COMPLETED_TOURNAMENT,
    REWARD_TOURNAMENT,
    UPDATE_INDEXED_ID,
    UPDATE_ENTITY_OWNER_AND_ACL,
    MAKE_SYSTEM_ENTITY,

    //GlobalFile
    GET_FILE_INFO,
    GET_FILE_INFO_SIMPLE,
    GET_GLOBAL_CDN_URL,
    GET_GLOBAL_FILE_LIST,

    SEND_CRASH_REPORT,    
    
    GET_BLOCKCHAIN_ITEMS,
    GET_UNIQS,

    POST_GROUP_SCORE_DYNAMIC,

    CREATE_ENTITY,
    DELETE_ENTITY,
    GET_COUNT,
    READ_ENTITY,
    UPDATE_ENTITY,
    UPDATE_ENTITY_FIELDS,
    UPDATE_ENTITY_FIELDS_SHARDED,
    DELETE_ENTITIES,
    UPDATE_SINGLETON_FIELDS,
    INCREMENT_DATA,

    CLEAR_USER_STATUS,
    EXTEND_USER_STATUS,
    GET_USER_STATUS,
    SET_USER_STATUS,

    CREATE_WITH_INDEXED_ID,
    DEREGISTER,
    DEREGISTER_ALL,
    REGISTER,
    SEND_SIMPLE,
    SEND_RICH,
    SEND_RAW,
    SEND_RAW_BATCH,
    SEND_RAW_TO_GROUP,
    SEND_TEMPLATED_TO_GROUP,
    SEND_NORMALIZED_TO_GROUP,
    SEND_NORMALIZED,
    SEND_NORMALIZED_BATCH,
    SCHEDULE_RICH_NOTIFICATION,
    SCHEDULE_NORMALIZED_NOTIFICATION,
    SCHEDULE_RAW_NOTIFICATION,

    FULL_PLAYER_RESET,
    GAME_DATA_RESET,    

    FIND_PLAYERS,
    FIND_PLAYERS_USING_FILTER,
    ENABLE_FOR_MATCH,
    DISABLE_FOR_MATCH,
    SHIELD_OFF,
    SHIELD_ON,
    SHIELD_ON_FOR,
    INCREMENT_SHIELD_ON_FOR,
    GET_SHIELD_EXPIRY,
    DECREMENT_PLAYER_RATING,
    INCREMENT_PLAYER_RATING,
    RESET_PLAYER_RATING,
    SET_PLAYER_RATING,

    START_MATCH,
    CANCEL_MATCH,
    COMPLETE_MATCH,

    TRIGGER,
    TRIGGER_MULTIPLE,

    GET_INVENTORY,
    AWARD_VC,
    RESET_PLAYER_VC,
    CONSUME_VC,
    GET_PLAYER_VC,
    START_STEAM_TRANSACTION,
    FINALIZE_STEAM_TRANSACTION,
    VERIFY_MICROSOFT_RECEIPT,
    ELIGIBLE_PROMOTIONS,
    CONFIRM_GOOGLEPLAY_PURCHASE,
    AWARD_PARENT_VC,
    CONSUME_PARENT_VC,
    GET_PARENT_VC,
    RESET_PARENT_VC,
    GET_PEER_VC,
    VERIFY_PURCHASE,
    START_PURCHASE,
    FINALIZE_PURCHASE,
    REFRESH_PROMOTIONS,

    GET_UPDATED_FILES,
    GET_FILE_LIST,

    RUN,
    SCHEDULE_CLOUD_SCRIPT,
    GET_SCHEDULED_CLOUD_SCRIPTS,
    GET_RUNNING_OR_QUEUED_CLOUD_SCRIPTS,
    RUN_PARENT_SCRIPT,
    CANCEL_SCHEDULED_SCRIPT,
    RUN_PEER_SCRIPT,
    RUN_PEER_SCRIPT_ASYNC,

    UPDATE_LANGUAGE_CODE,
    UPDATE_TIMEZONE_OFFSET,

    READ_FRIEND_PLAYER_STATE,
    LIST_FRIENDS,
    ADD_FRIENDS,
    ADD_FRIENDS_FROM_PLATFORM,
    REMOVE_FRIENDS,
    GET_MY_SOCIAL_INFO,
    GET_USERS_ONLINE_STATUS,

    DELETE_SENT,
    
    UPDATE_NAME,
    
    LOGOUT,

    GET_FRIEND_PROFILE_INFO_FOR_EXTERNAL_ID,
    GET_PROFILE_INFO_FOR_CREDENTIAL,
    GET_PROFILE_INFO_FOR_EXTERNAL_AUTH_ID,
    GET_EXTERNAL_ID_FOR_PROFILE_ID,
    FIND_PLAYER_BY_NAME,
    FIND_USERS_BY_EXACT_NAME,
    FIND_USERS_BY_SUBSTR_NAME,
    FIND_PLAYER_BY_UNIVERSAL_ID,
    FIND_USER_BY_EXACT_UNIVERSAL_ID,
    FIND_USERS_BY_NAME_STARTING_WITH,
    FIND_USERS_BY_UNIVERSAL_ID_STARTING_WITH,
    GET_SUMMARY_DATA_FOR_PROFILE_ID,
    READ_FRIEND_ENTITY,
    READ_FRIENDS_ENTITIES,
    READ_FRIENDS_WITH_APPLICATION,
    READ_BY_TYPE,
    UPDATE_TIME_TO_LIVE,

    UPDATE_SINGLETON,
    DELETE_SINGLETON,

    GET_ATTRIBUTES,
    UPDATE_ATTRIBUTES,
    REMOVE_ATTRIBUTES,
    UPDATE_PICTURE_URL,
    UPDATE_CONTACT_EMAIL,

    READ_PROPERTIES,
    READ_SELECTED_PROPERTIES,
    READ_PROPERTIES_IN_CATEGORIES,

    START_STREAM,
    READ_STREAM,
    END_STREAM,
    DELETE_STREAM,
    ADD_EVENT,
    GET_STREAM_SUMMARIES_FOR_INITIATING_PLAYER,
    GET_STREAM_SUMMARIES_FOR_TARGET_PLAYER,
    GET_RECENT_STREAMS_FOR_INITIATING_PLAYER,
    GET_RECENT_STREAMS_FOR_TARGET_PLAYER,

    REDEEM_CODE,
    GET_REDEEMED_CODES,

    CUSTOM_PAGE_EVENT,
    CUSTOM_SCREEN_EVENT,
    CUSTOM_TRACK_EVENT,

    // profanity
    PROFANITY_CHECK,
    PROFANITY_REPLACE_TEXT,
    PROFANITY_IDENTIFY_BAD_WORDS,

    //file
    PREPARE_USER_UPLOAD,
    LIST_USER_FILES,
    DELETE_USER_FILE,
    DELETE_USER_FILES,
    GET_CDN_URL,

    //group file
    CHECK_FILENAME_EXISTS,
    CHECK_FULLPATH_FILENAME_EXISTS,
    COPY_FILE,
    DELETE_FILE,
    MOVE_FILE,
    MOVE_USER_TO_GROUP_FILE,
    UPDATE_FILE_INFO,

    //mail
    SEND_BASIC_EMAIL,
    SEND_ADVANCED_EMAIL,
    SEND_ADVANCED_EMAIL_BY_ADDRESS,

    //presence
    FORCE_PUSH,
    GET_PRESENCE_OF_FRIENDS,
    GET_PRESENCE_OF_GROUP,
    GET_PRESENCE_OF_USERS,
    REGISTER_LISTENERS_FOR_FRIENDS,
    REGISTER_LISTENERS_FOR_GROUP,
    REGISTER_LISTENERS_FOR_PROFILES,
    SET_VISIBILITY,
    STOP_LISTENING,
    UPDATE_ACTIVITY,

    //tournament
    CLAIM_TOURNAMENT_REWARD,
    GET_DIVISION_INFO,
    GET_MY_DIVISIONS,
    GET_TOURNAMENT_STATUS,
    JOIN_DIVISION,
    JOIN_TOURNAMENT,
    LEAVE_DIVISION_INSTANCE,
    LEAVE_TOURNAMENT,
    POST_TOURNAMENT_SCORE,
    POST_TOURNAMENT_SCORE_WITH_RESULTS,
    VIEW_CURRENT_REWARD,
    VIEW_REWARD,

    //chat
    CHANNEL_CONNECT,
    CHANNEL_DISCONNECT,
    DELETE_CHAT_MESSAGE,
    GET_CHANNEL_ID,
    GET_CHANNEL_INFO,
    GET_CHAT_MESSAGE,
    GET_RECENT_CHAT_MESSAGES,
    GET_SUBSCRIBED_CHANNELS,
    POST_CHAT_MESSAGE,
    POST_CHAT_MESSAGE_SIMPLE,
    UPDATE_CHAT_MESSAGE,

    //rtt
    REQUEST_CLIENT_CONNECTION,

    //lobby
    CREATE_LOBBY,
    CREATE_LOBBY_WITH_PING_DATA,
    FIND_LOBBY,
    FIND_LOBBY_WITH_PING_DATA,
    FIND_OR_CREATE_LOBBY,
    FIND_OR_CREATE_LOBBY_WITH_PING_DATA,
    GET_LOBBY_DATA,
    LEAVE_LOBBY,
    JOIN_LOBBY,
    JOIN_LOBBY_WITH_PING_DATA,
    REMOVE_MEMBER,
    SEND_SIGNAL,
    SWITCH_TEAM,
    UPDATE_READY,
    UPDATE_SETTINGS,
    CANCEL_FIND_REQUEST,
    GET_REGIONS_FOR_LOBBIES,
    PING_REGIONS,
    GET_LOBBY_INSTANCES,
    GET_LOBBY_INSTANCES_WITH_PING_DATA,

    //messaging
    DELETE_MESSAGES,
    GET_MESSAGE_BOXES,
    GET_MESSAGE_COUNTS,
    GET_MESSAGES,
    GET_MESSAGES_PAGE,
    GET_MESSAGES_PAGE_OFFSET,
    MARK_MESSAGES_READ,
    SEND_MESSAGE,
    SEND_MESSAGE_SIMPLE,

    //item Catalog
    GET_CATALOG_ITEM_DEFINITION,
	GET_CATALOG_ITEMS_PAGE,
    GET_CATALOG_ITEMS_PAGE_OFFSET,
    
    //userItems
	AWARD_USER_ITEM,
	DROP_USER_ITEM,
	GET_USER_ITEMS_PAGE,
	GET_USER_ITEMS_PAGE_OFFSET,
	GET_USER_ITEM,
	GIVE_USER_ITEM_TO,
	PURCHASE_USER_ITEM,
	RECEIVE_USER_ITEM_FROM,
	SELL_USER_ITEM,
	UPDATE_USER_ITEM_DATA,
    USE_USER_ITEM,
    REFRESH_BLOCKCHAIN_USER_ITEMS,
    PUBLISH_USER_ITEM_TO_BLOCKCHAIN,
    REMOVE_USER_ITEM_FROM_BLOCKCHAIN
}
