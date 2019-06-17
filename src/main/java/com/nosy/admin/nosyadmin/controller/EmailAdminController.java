package com.nosy.admin.nosyadmin.controller;

import com.nosy.admin.nosyadmin.dto.EmailTemplateDto;
import com.nosy.admin.nosyadmin.dto.InputSystemDto;
import com.nosy.admin.nosyadmin.model.EmailProviderProperties;
import com.nosy.admin.nosyadmin.model.EmailTemplate;
import com.nosy.admin.nosyadmin.model.ReadyEmail;
import com.nosy.admin.nosyadmin.service.EmailTemplateService;
import com.nosy.admin.nosyadmin.service.InputSystemService;
import com.nosy.admin.nosyadmin.utils.EmailTemplateMapper;
import com.nosy.admin.nosyadmin.utils.InputSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/api/v1/nosy")
public class EmailAdminController {
  private EmailTemplateService emailTemplateService;
  private InputSystemService inputSystemService;

  @Autowired
  public EmailAdminController(
      EmailTemplateService emailTemplateService,
      InputSystemService inputSystemService) {
    this.emailTemplateService = emailTemplateService;
    this.inputSystemService = inputSystemService;
  }

  @PostMapping(value = "/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}/post")
  public ResponseEntity<EmailTemplateDto> emailTemplatePost(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      @RequestBody EmailProviderProperties emailProviderProperties,
      Principal principal) {

    return new ResponseEntity<>(
        EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplateService.postEmailTemplate(
            inputSystemId, emailTemplateId, emailProviderProperties, principal.getName())),
        HttpStatus.OK);
  }

  @PostMapping(value = "/email/post")
  public ResponseEntity<EmailTemplateDto> emailPost(@RequestBody ReadyEmail readyEmail) {
      return new ResponseEntity<>(
              EmailTemplateMapper.INSTANCE.toEmailTemplateDto(emailTemplateService.postEmail(readyEmail)), HttpStatus.OK);
  }

  @PostMapping(value = "/inputsystems", consumes = "application/json")
  public ResponseEntity<InputSystemDto> newType(@RequestBody InputSystemDto inputSystemDto, Principal principal) {
    return new ResponseEntity<>( InputSystemMapper.INSTANCE.toInputSystemDto(inputSystemService.saveInputSystem(
        InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto), principal.getName())), HttpStatus.CREATED);
  }

  @GetMapping(value = "/inputsystems")
  public ResponseEntity<Set<InputSystemDto>> getInputSystems(Principal principal) {
    return new ResponseEntity<>(
            InputSystemMapper.INSTANCE.toInputSystemDtoSet(inputSystemService.getListOfInputSystems(principal.getName())), HttpStatus.OK);
  }

  @GetMapping(value = "/inputsystems/emailproviders")
  public ResponseEntity<List<String>> getEmailAllProviders(Principal principal) {
    return new ResponseEntity<>(emailTemplateService.getAllEmailProviders(), HttpStatus.OK);
  }

  @PutMapping(value = "/inputsystems/{inputSystemId}")
  public ResponseEntity<InputSystemDto> updateInputSystemName(
      @PathVariable String inputSystemId,
      @RequestBody InputSystemDto inputSystemDto,
      Principal principal) {

    return new ResponseEntity<>(
            InputSystemMapper.INSTANCE.toInputSystemDto(inputSystemService.updateInputSystemStatus(
            inputSystemId, InputSystemMapper.INSTANCE.toInputSystem(inputSystemDto), principal.getName())),
        HttpStatus.OK);
  }

  @PostMapping(value = "/inputsystems/{inputSystemId}/emailtemplates")
  public ResponseEntity<EmailTemplateDto> newEmailTemplate(
      @PathVariable String inputSystemId,
      @RequestBody EmailTemplateDto emailTemplateDto,
      Principal principal) {
    return new ResponseEntity<>( EmailTemplateMapper.INSTANCE.toEmailTemplateDto(
        emailTemplateService.newEmailTemplate(
                EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto),
            inputSystemId,
            principal.getName())),
        HttpStatus.CREATED);
  }

  @GetMapping(value = "/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}")
  public ResponseEntity<EmailTemplate> getEmailTemplateByInputSystemAndEmailTemplateId(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      Principal principal) {
    return new ResponseEntity<>(
        emailTemplateService.getEmailTemplateById(
            emailTemplateId, inputSystemId, principal.getName()),
        HttpStatus.OK);
  }

  @PutMapping(value = "/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}")
  public ResponseEntity<EmailTemplateDto> updateEmailTemplate(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      @RequestBody EmailTemplateDto emailTemplateDto,
      Principal principal) {
    return new ResponseEntity<>(  EmailTemplateMapper.INSTANCE.toEmailTemplateDto(
        emailTemplateService.updateEmailTemplate(
                EmailTemplateMapper.INSTANCE.toEmailTemplate(emailTemplateDto),
            inputSystemId,
            emailTemplateId,
            principal.getName())),
        HttpStatus.OK);
  }

  @GetMapping(value = "/inputsystems/{inputSystemId}/emailtemplates")
  public ResponseEntity<List<EmailTemplateDto>> getEmailTemplates(
      @PathVariable String inputSystemId, Principal principal) {
    return new ResponseEntity<>(EmailTemplateMapper.INSTANCE.toEmailTemplateDtoList(
        emailTemplateService.getListOfEmailTemplates(inputSystemId, principal.getName())),
        HttpStatus.OK);
  }

  @DeleteMapping(value = "/inputsystems/{inputSystemId}/emailtemplates/{emailTemplateId}")
  public ResponseEntity<String> deleteEmailTemplate(
      @PathVariable String inputSystemId,
      @PathVariable String emailTemplateId,
      Principal principal) {
    emailTemplateService.deleteEmailTemplate(inputSystemId, emailTemplateId, principal.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(value = "/inputsystems/{inputSystemId}")
  public ResponseEntity<String> deleteInputSystem(
      @PathVariable String inputSystemId, Principal principal) {
    inputSystemService.deleteInputSystem(inputSystemId, principal.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


}
