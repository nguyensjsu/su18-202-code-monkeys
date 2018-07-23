package edu.sjsu.cmpe202.starbucks.core.service.user.datastore;

import com.google.cloud.datastore.*;
import edu.sjsu.cmpe202.starbucks.beans.User;
import edu.sjsu.cmpe202.starbucks.core.service.user.UserService;

public class DatastoreUserService implements UserService {
    private static final String Kind = "User";
    private Datastore datastore;

    public DatastoreUserService() {
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }

    public boolean insertUser(User user) {
        Key userKey = this.datastore.newKeyFactory()
                .setKind(Kind)
                .newKey(user.getProfile());

        Entity e = datastore.get(userKey);
        if (e == null) {
            // Create a new entity and insert it
            FullEntity newUser = Entity.newBuilder(userKey)
                    .set("firstName", user.getFirstName())
                    .set("lastName", user.getLastName())
                    .build();
            e = datastore.add(newUser);
            if (e == null) {
                return false;
            }
        }

        return true;
    }
}
