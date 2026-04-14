package at.simcc.simcc_backend.generator;

import at.simcc.simcc_backend.entities.User;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 10.04.2026
 * Time: 10:29
 */
public class CustomIdGenerator implements IdentifierGenerator {
    /**
     * If it is an admin account, it will set the Id to 0, and in all other case will generate a random UUID
     * @param sharedSessionContractImplementor
     * @param o
     * @return
     */

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        if (o instanceof User user){
            if (user.getIsAdmin()){
                return 0L;
            }
        }

        return Math.abs(UUID.randomUUID().getLeastSignificantBits());
    }
}
