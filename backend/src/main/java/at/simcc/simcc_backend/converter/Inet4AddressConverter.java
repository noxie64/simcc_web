package at.simcc.simcc_backend.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Project: backend
 * Created by: Marko Kushlyk
 * Date: 12.05.2026
 * Time: 10:14
 */
@Converter(autoApply = true)
public class Inet4AddressConverter implements AttributeConverter<Inet4Address, String> {
    @Override
    public String convertToDatabaseColumn(Inet4Address inet4Address) {
        if (inet4Address == null) return null;
        return inet4Address.getHostAddress();
    }

    @Override
    public Inet4Address convertToEntityAttribute(String s) {
        if (s == null) return null;
        try{
            return (Inet4Address) Inet4Address.getByName(s);
        }catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
