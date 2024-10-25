package tech.engix.auth_service.service.validation.auth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import tech.engix.auth_service.controller.exception.FieldMessage;
import tech.engix.auth_service.dto.SignUpDto;
import tech.engix.auth_service.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserIsValidValidation implements ConstraintValidator<UserInsertValid, SignUpDto> {

    private final UserRepository repository;

    @Override
    public void initialize(UserInsertValid constraintAnnotation) {
        // comment explaining why the method is empty
    }

    @Override
    public boolean isValid(SignUpDto userInsertDTO, ConstraintValidatorContext constraintValidatorContext) {
        List<FieldMessage> list = new ArrayList<>();

        if (repository.findByEmail(userInsertDTO.email()) != null) {
            list.add(new FieldMessage("email", "Email already exists"));
        }

        for (FieldMessage e : list) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}