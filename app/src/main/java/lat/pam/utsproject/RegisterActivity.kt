package lat.pam.utsproject

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var passwordWarningTextView: TextView
    private lateinit var backToLoginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Menghubungkan komponen tampilan
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        confirmPasswordEditText = findViewById(R.id.etConfirmPassword)
        registerButton = findViewById(R.id.btnRegister)
        passwordWarningTextView = findViewById(R.id.tvPasswordWarning)
        backToLoginTextView = findViewById(R.id.tvBackToLogin)
        val text = "Sudah punya akun? Masuk disini"
        val spannableString = SpannableString(text)

// Tentukan start dan end untuk bagian "Daftar disini"
        val startIndex = text.indexOf("Masuk disini")
        val endIndex = startIndex + "Masuk disini".length

// Mengatur warna dan ketebalan hanya pada "Daftar disini"
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.holo_orange_dark)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        backToLoginTextView.text = spannableString

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set onClickListener untuk tombol registrasi
        registerButton.setOnClickListener {
            val inputEmail = emailEditText.text.toString().trim()
            val inputPassword = passwordEditText.text.toString().trim()
            val inputConfirmPassword = confirmPasswordEditText.text.toString().trim()

            // Validasi input
            if (inputEmail.isEmpty() || inputPassword.isEmpty() || inputConfirmPassword.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi password
            if (!isPasswordValid(inputPassword)) {
                Toast.makeText(this, "Password harus minimal 6 karakter dan mengandung huruf dan angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi konfirmasi password
            if (inputPassword != inputConfirmPassword) {
                passwordWarningTextView.text = "Password dan Konfirmasi Password tidak cocok"
                passwordWarningTextView.visibility = android.view.View.VISIBLE
                return@setOnClickListener
            } else {
                passwordWarningTextView.visibility = android.view.View.GONE
            }

            // Cek registrasi menggunakan Firebase
            auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                        finish() // Menutup RegisterActivity
                    } else {
                        Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        backToLoginTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Menutup RegisterActivity agar pengguna tidak kembali ke halaman ini
        }
    }

    // Fungsi untuk memvalidasi password
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6 && password.any { it.isDigit() } && password.any { it.isLetter() }
    }
}
