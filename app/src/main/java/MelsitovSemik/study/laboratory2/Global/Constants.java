package MelsitovSemik.study.laboratory2.Global;

public class Constants {

    public static final String REFRESH_MEETINGS_BROADCAST_ACTION = "refresh meetings";

    public static final class Meeting{
        public static final String NAME = "meetings";
        public static final class Cols{
            public static final String CREATOR = "creator";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String MEMBERS = "members";
            public static final String PRIORITY = "priority";
            public static final String DATE_START = "date_start";
            public static final String DATE_END = "date_end";
        }
    }

    public static final class Menu{
        public static final int SIGN_OUT = 1;
    }

    public static final class Broadcast{
        public static final String NOTIFICATION_ID = "1001";
    }

    public static final class VIEW_TYPES{
        public static final int HEADER = 1;
        public static final int NORMAL = 2;
    }

}
