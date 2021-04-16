package pe.edu.tecsup.tienda.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import pe.edu.tecsup.tienda.entities.Producto;
import pe.edu.tecsup.tienda.services.ProductoService;

@WebServlet("/CarritoOfertarServlet")
public class CarritoOfertarServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	private static final Logger log = Logger.getLogger(CarritoOfertarServlet.class);
	
	private ProductoService productoService;
	
	public CarritoOfertarServlet() {
		this.productoService = new ProductoService();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Get CarritoOfertarServlet");
		try {
			
			//String cat_id = request.getParameter("categoria_id");
			
			List<Producto> productos = null;
			
			//if (cat_id == null)						
				productos = productoService.listar();
			//else 
			//	productos = productoService.listarPorCategoria(cat_id);
			
			// 
			request.setAttribute("productos", productos);
			
			request.getRequestDispatcher("/WEB-INF/jsp/carrito/ofertar.jsp").forward(request, response);
	        
		} catch (Exception e) {
			log.error(e, e);
			throw new ServletException(e.getMessage(), e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
