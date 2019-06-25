package general;

public enum LicenceType {
    LICENCE_TYPE_AM, LICENCE_TYPE_A1, LICENCE_TYPE_A2, LICENCE_TYPE_A, LICENCE_TYPE_B, LICENCE_TYPE_B_A, LICENCE_TYPE_BE;

    public static String XMLNameOfType(LicenceType type){
        switch (type){
            case LICENCE_TYPE_AM: return "AM";
            case LICENCE_TYPE_A1: return "A1";
            case LICENCE_TYPE_A2: return "A2";
            case LICENCE_TYPE_A: return "A";
            case LICENCE_TYPE_B: return "B";
            case LICENCE_TYPE_B_A: return "B(A)";
            case LICENCE_TYPE_BE: return "BE";
        }
        return "";
    }

    public static LicenceType TypeOfXMLName(String type){
        switch (type){
            case "AM": return LICENCE_TYPE_AM;
            case "A1": return LICENCE_TYPE_A1;
            case "A2": return LICENCE_TYPE_A2;
            case "A": return LICENCE_TYPE_A ;
            case "B": return LICENCE_TYPE_B ;
            case "B(A)": return LICENCE_TYPE_B_A;
            case "BE": return LICENCE_TYPE_BE;
        }
        return null;
    }
}
