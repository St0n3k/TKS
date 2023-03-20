package pl.lodz.p.it.tks.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.jws.WebService;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.tks.model.RoomSoapDTO;
import pl.lodz.p.it.tks.ui.query.RoomQueryUseCase;

import java.util.List;
import java.util.stream.Collectors;

@WebService(serviceName = "roomAPI", endpointInterface = "pl.lodz.p.it.tks.service.RoomSoapService")
@RequestScoped
@NoArgsConstructor
public class RoomSoapServiceImpl implements RoomSoapService {

    @Inject
    private RoomQueryUseCase roomQueryUseCase;


    @Override
    public List<RoomSoapDTO> getAllRooms() {
        return roomQueryUseCase.getAllRooms()
                .stream().map(RoomSoapDTO::new)
                .collect(Collectors.toList());
    }

}
