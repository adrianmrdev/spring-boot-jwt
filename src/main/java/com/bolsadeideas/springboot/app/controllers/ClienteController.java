package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import com.bolsadeideas.springboot.app.view.xml.ClienteList;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private MessageSource messageSource;
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@GetMapping("/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_OTRO')")
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable("id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		// Cliente cliente = clienteService.findOne(id);

		Cliente cliente = clienteService.fetchByIdWithFacturas(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		return "ver";

	}
	
	@GetMapping({ "/listar", "/" })
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale) {

		if (authentication != null) {
			logger.info("Hola usuario autenticado, tu username es: " + authentication.getName());
		}

		// FORMA 1: con metodo propio hasRole
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			logger.info("De forma estática: Hola usuario autenticado, tu username es: " + auth.getName());
		}

		if (hasRole("ROLE_ADMIN")) {
			logger.info("Hola " + auth.getName() + " tienes acceso!");
		} else {
			logger.info("Hola " + auth.getName() + " NO tienes acceso!");
		}

		// FORMA 2: con security context
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if(securityContext.isUserInRole("ADMIN")) {
			logger.info("Hola " + auth.getName() + " tienes acceso (chequeado con security context)!");
		} else {
			logger.info("Hola " + auth.getName() + " NO tienes acceso (chequeado con security context)!");
		}
		
		// FORMA 3: con el request directamente
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Hola " + auth.getName() + " tienes acceso (chequeado con request)!");
		} else {
			logger.info("Hola " + auth.getName() + " NO tienes acceso (chequeado con request)!");
		}
		
		Pageable pageRequest = PageRequest.of(page, 4);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@GetMapping("/listar-rest")
	public @ResponseBody ClienteList listarRest() {
		// Utilizamos clase wapper ClienteList para poder devolver XML (lo hará por defecto)
		return new ClienteList(clienteService.findAll());
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/form")
	public String crear(Map<String, Object> model) {

		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				uploadFileService.delete(cliente.getFoto());

			}

			String uniqueFileName = null;

			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Ha subido correctamente la imagen " + foto.getOriginalFilename());
			cliente.setFoto(uniqueFileName);
		}

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", "Cliente creado correctamente!");
		return "redirect:listar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/form/{id}")
	public String editar(@PathVariable("id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El id del cliente no existe en base de datos!");
			}
		} else {
			flash.addFlashAttribute("error", "El id del cliente no pude ser 0!");
			return "redirect:listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);

			flash.addFlashAttribute("success", "Cliente eliminado correctamente!");

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto del cliente eliminado correctamente!");
			}

		}
		return "redirect:/listar";

	}

	public boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication authentication = context.getAuthentication();

		if (authentication == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		return authorities.contains(new SimpleGrantedAuthority(role));
		
		/*for (GrantedAuthority authority : authorities) {
			if (role.equals(authority.getAuthority())) {
				logger.info("Hola " + authentication.getName() + ", tu rol es: " + authority.getAuthority());
				return true;
			}
		}

		return false;*/
	}

}
