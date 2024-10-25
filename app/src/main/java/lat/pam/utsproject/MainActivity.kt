package lat.pam.utsproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Menghubungkan komponen tampilan
        val emailEditText = findViewById<EditText>(R.id.etUsername)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerTextView = findViewById<TextView>(R.id.tvRegister)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set onClickListener untuk tombol login
        loginButton.setOnClickListener {
            val inputEmail = emailEditText.text.toString()
            val inputPassword = passwordEditText.text.toString()

            // Cek autentikasi menggunakan Firebase
            auth.signInWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Jika login berhasil
                        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ListFoodActivity::class.java)
                        startActivity(intent)
                        finish() // Menutup MainActivity agar pengguna tidak kembali ke halaman login
                    } else {
                        // Jika login gagal
                        Toast.makeText(
                            this,
                            "Login gagal: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets

            }
        }
    }