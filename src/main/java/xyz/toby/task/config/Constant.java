package xyz.toby.task.config;

/**
 * @Author: zhangbin
 * @Date: 2020/12/17
 */
public class Constant {
    public static final String METHOD_NAME_DELAYED = "performDelayed";

    public static final String METHOD_NAME_ASYNC = "performAsync";

    public static final String METHOD_NAME_DEPENDENT = "performDependent";

    public static final String METHOD_NAME_TRANSFERABLE = "performTransferable";

    public static final String METHOD_NAME_GET_BIZ_ID = "getBizId";


    public static final int LUA_RESULT_FAILED = 0;
    public static final int LUA_RESULT_OK = 1;
    public static final int LUA_RESULT_REPEATED_BIZ_ID = 2;
}
