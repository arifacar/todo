package com.arifacar.domain.model.constants;

public class ResponseCodes {

    private ResponseCodes() {
        throw new IllegalStateException(ErrorConstants.UTILITY_CLASS);
    }

    public static final int SUCCESS = 10;                //10-29 ->  Group of Success

    public static final int SUCCESS_WITH_POPUP = 30;    //30-39 ->  Group of Success With Popup

    public static final int FAIL_WITH_POPUP = 50;        //50-59 ->  Group of Failed With Popup

    public static final int FAIL = 70;                    //70-79 ->  Group of Failed

}
