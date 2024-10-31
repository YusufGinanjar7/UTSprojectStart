package lat.pam.utsproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmation)

        // Mengambil data dari Intent
        val foodNames = intent.getStringExtra("FOOD_NAMES")
        val totalServings = intent.getStringExtra("TOTAL_SERVINGS")
        val orderName = intent.getStringExtra("ORDER_NAME")
        val notes = intent.getStringExtra("NOTES")

        // Menampilkan data di TextView
        findViewById<TextView>(R.id.tvTitle).text = "Order Confirmation"
        findViewById<TextView>(R.id.foodNameTextView).text = "Food Name: $foodNames"
        findViewById<TextView>(R.id.servingsTextView).text = "Number of Servings: $totalServings"
        findViewById<TextView>(R.id.orderNameTextView).text = "Ordering Name: $orderName"
        findViewById<TextView>(R.id.notesTextView).text = "Additional Notes: $notes"

        // Mengambil username dari Intent
        val username = intent.getStringExtra("USERNAME")

        // Back to Menu button
        val backToMenuButton = findViewById<Button>(R.id.backtoMenu)
        backToMenuButton.setOnClickListener {
            val intent = Intent(this, ListFoodActivity::class.java)
            intent.putExtra("USERNAME", username) // Kirim username kembali
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
