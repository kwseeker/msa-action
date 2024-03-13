package top.kwseeker.msa.action.user.trigger.http.model.dto.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.kwseeker.msa.action.user.domain.auth.model.vo.LoginReqVO;
import top.kwseeker.msa.action.user.domain.auth.model.vo.UserCreateVO;
import top.kwseeker.msa.action.user.trigger.http.model.dto.LoginDTO;
import top.kwseeker.msa.action.user.trigger.http.model.dto.UserCreateDTO;

@Mapper
public interface Converter {

    Converter INSTANCE = Mappers.getMapper(Converter.class);

    LoginReqVO convert(LoginDTO loginDTO);

    UserCreateVO convert(UserCreateDTO userCreateDTO);
}
