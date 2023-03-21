package pl.lodz.p.it.tks.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tks.model.Apartment;
import pl.lodz.p.it.tks.model.ApartmentSoapDTO;
import pl.lodz.p.it.tks.model.Room;
import pl.lodz.p.it.tks.model.RoomSoapDTO;

class RoomSoapMapperTest {

    private static final RoomSoapMapper mapper = new RoomSoapMapper();

    @Test
    void mapRoomToDtoTest() {
        Room room = new Room(1, 57.0, 2);

        RoomSoapDTO dto = mapper.mapToDto(room);
        assertNotNull(dto);
        assertEquals(1, dto.getRoomNumber());
        assertEquals(57.0, dto.getPrice(), 0.01);
        assertEquals(2, dto.getSize());
    }

    @Test
    void mapApartmentToDtoTest() {
        Room room = new Apartment(2, 58.1, 3, 34.34);

        RoomSoapDTO dto = mapper.mapToDto(room);
        assertNotNull(dto);
        assertTrue(dto instanceof ApartmentSoapDTO);

        ApartmentSoapDTO apartmentSoapDTO = (ApartmentSoapDTO) dto;
        assertEquals(2, dto.getRoomNumber());
        assertEquals(58.1, dto.getPrice(), 0.01);
        assertEquals(3, dto.getSize());
        assertEquals(34.34, apartmentSoapDTO.getBalconyArea(), 0.01);
    }
}
