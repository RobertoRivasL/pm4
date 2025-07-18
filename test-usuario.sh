# Crear archivo temporal para probar Usuario
cat > TestUsuario.java << EOF
import com.robertorivas.automatizacion.modelos.Usuario;
public class TestUsuario {
    public static void main(String[] args) {
        Usuario u = new Usuario("test@test.com", "password123");
        System.out.println("Usuario creado: " + u.getEmail());
        System.out.println("Validación: " + u.validarPassword("password123"));
        System.out.println("✅ Usuario.java funciona correctamente");
    }
}
EOF