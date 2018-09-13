package GenericTest;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        CommonServiceImpl<CommonResource<String>> commonService = new CommonServiceImpl<>();
        commonService.addAll(new ArrayList<>(),new CommonResource<String>(),new CommonResource<String>());

        Class<String> str = String.class;

    }

}
