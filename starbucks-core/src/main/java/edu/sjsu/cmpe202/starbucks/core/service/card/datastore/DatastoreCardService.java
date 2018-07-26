package edu.sjsu.cmpe202.starbucks.core.service.card.datastore;

import com.google.cloud.datastore.*;
import edu.sjsu.cmpe202.starbucks.beans.Card;
import edu.sjsu.cmpe202.starbucks.core.service.card.CardService;

import java.util.ArrayList;
import java.util.List;

public class DatastoreCardService implements CardService {
    private static final String Kind = "Card";
    private Datastore datastore;

    public DatastoreCardService(){
        this.datastore = DatastoreOptions.getDefaultInstance().getService();
    }

    public boolean addCard(Card card) throws Exception {
        if (this.exists(card)) {
            throw new Exception("card already exists");
        }

        FullEntity newCard = this.getEntity(card);
        Entity e = datastore.add(newCard);
        if (e == null) {
            return false;
        }

        return true;
    }

    public boolean updateCard(Card card) {
        if (! this.exists(card)) {
            return false;
        }

        Entity e = this.getEntity(card);
        datastore.update(e);
        return true;
    }

    public boolean exists(Card card) {
        Card c = this.getCard(card.getId(), card.getUser());
        if (c == null) {
            return false;
        }

        return true;
    }

    public boolean deleteCard(Card card) {
        if (! this.exists(card)) {
            return false;
        }

        Key cardKey = getKey(card);

        this.datastore.delete(cardKey);
        return true;
    }

    public Card getCard(String card, String user) {
        Card temp = new Card(card, user, "", 0f);
        Key cardKey = this.getKey(temp);
        Entity e = datastore.get(cardKey);

        if (e == null) {
            return null;
        }

        Card c = this.getCardFromEntity(e);
        c.setId(card);
        c.setUser(user);

        return c;
    }

    public List<Card> getCards(String user) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(Kind)
                .setFilter(StructuredQuery.PropertyFilter.hasAncestor(
                        datastore.newKeyFactory().setKind("User").newKey(user)))
                .build();

        QueryResults<Entity> entities = datastore.run(query);
        List<Card> cards = new ArrayList<Card>();

        while (entities.hasNext()) {
            Entity e = entities.next();
            cards.add(this.getCardFromEntity(e));
        }

        return cards;
    }

    private Key getKey(Card card) {
        return this.datastore.newKeyFactory()
                .setKind(Kind)
                .addAncestor(PathElement.of("User", card.getUser()))
                .newKey(card.getId());
    }

    private Entity getEntity(Card card) {
        Key cardKey = getKey(card);

        Entity newCard = Entity.newBuilder(cardKey)
                .set("cvv", card.getCvv())
                .set("balance", card.getBalance())
                .build();

        return newCard;
    }

    private Card getCardFromEntity(Entity entity) {
        return new Card(entity.getKey().getName(), "", entity.getString("cvv"), entity.getDouble("balance"));
    }


}
