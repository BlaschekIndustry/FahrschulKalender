package general;

public class User {
    public static final String XML_USER_GROUP_IDENT = "users";
    public static final String XML_USERR_IDENT = "user";
    public static final String XML_USER_NAME_IDENT = "uName";
    public static final String XML_USER_ALLOW_EDIT_VEHICLES = "uAllowEditVehicles";
    public static final String XML_USER_ALLOW_EDIT_TEACHERS = "uAllowEditTeachers";
    public static final String XML_USER_ALLOW_EDIT_USERS = "uAllowEditUsers";
    public static final String XML_USER_ALLOW_INSERT_OTHER_EVENTS = "uAllowInsertOtherEvents";

    private String name;
    private boolean editVehicles;
    private boolean editTeachers;
    private boolean editUsers;
    private boolean insertOtherEvents;

    public User(){
        this("Neuer Benutzer", false, false, false, false);
    }

    public User(String name, boolean editVehicles, boolean editTeachers, boolean editUsers, boolean insertOtherEvents) {
        this.name = name;
        this.editVehicles = editVehicles;
        this.editTeachers = editTeachers;
        this.editUsers = editUsers;
        this.insertOtherEvents = insertOtherEvents;
    }

    public String getName() {
        return name;
    }

    public boolean canEditVehicles() {
        return editVehicles;
    }

    public boolean canEditTeachers() {
        return editTeachers;
    }

    public boolean canEditUsers() {
        return editUsers;
    }

    public boolean canInsertOtherEvents() {
        return insertOtherEvents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEditVehicles(boolean editVehicles) {
        this.editVehicles = editVehicles;
    }

    public void setEditTeachers(boolean editTeachers) {
        this.editTeachers = editTeachers;
    }

    public void setEditUsers(boolean editUsers) {
        this.editUsers = editUsers;
    }

    public void setInsertOtherEvents(boolean insertOtherEvents) {
        this.insertOtherEvents = insertOtherEvents;
    }
}
