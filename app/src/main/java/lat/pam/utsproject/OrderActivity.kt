package lat.pam.utsproject

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order)

        val selectedFoods = intent.getParcelableArrayListExtra<Food>("selected_food_list") ?: arrayListOf()
        val foodContainer = findViewById<LinearLayout>(R.id.foodContainer)
        foodContainer.removeAllViews()
        val username = intent.getStringExtra("USERNAME")

        // Setup judul untuk kolom
        val titleLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 8, 16, 8)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(ContextCompat.getColor(this@OrderActivity, android.R.color.holo_orange_light)) // Kuning halus
        }

        val foodNameTitle = TextView(this).apply {
            text = "Nama Makanan"
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f // Menggunakan weight untuk mengisi ruang
            )
            gravity = Gravity.START
            textSize = 18f
            setTextColor(ContextCompat.getColor(this@OrderActivity, android.R.color.white))
        }

        val servingsTitle = TextView(this).apply {
            text = "Jumlah"
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f // Menggunakan weight untuk mengisi ruang
            )
            gravity = Gravity.END
            textSize = 18f
            setTextColor(ContextCompat.getColor(this@OrderActivity, android.R.color.white))
        }

        titleLayout.addView(foodNameTitle)
        titleLayout.addView(servingsTitle)
        foodContainer.addView(titleLayout)

        // Menampilkan setiap makanan dalam "baris tabel"
        for (food in selectedFoods) {
            val foodItemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(16, 8, 16, 8)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setBackgroundColor(ContextCompat.getColor(this@OrderActivity, android.R.color.background_light))
            }

            val foodNameTextView = TextView(this).apply {
                text = food.name
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // Menggunakan weight untuk mengisi ruang
                )
                gravity = Gravity.START
                textSize = 16f
                setPadding(0, 0, 16, 0)
            }

            val servingsTextView = TextView(this).apply {
                text = food.quantity.toString()
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // Menggunakan weight untuk mengisi ruang
                )
                gravity = Gravity.END
                textSize = 16f
                setPadding(16, 0, 0, 0)
            }

            foodItemLayout.addView(foodNameTextView)
            foodItemLayout.addView(servingsTextView)

            // Garis bawah sebagai pemisah setiap baris
            val separator = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2 // Tinggi garis bawah
                ).also { params ->
                    (params as LinearLayout.LayoutParams).setMargins(16, 0, 16, 0)
                }
                setBackgroundColor(ContextCompat.getColor(this@OrderActivity, android.R.color.darker_gray))
            }

            // Tambahkan item makanan dan separator ke container
            foodContainer.addView(foodItemLayout)
            foodContainer.addView(separator) // Garis ditambahkan di sini setelah setiap item
        }

        val btnOrder = findViewById<Button>(R.id.btnOrder)
        btnOrder.setOnClickListener {
            val orderName = findViewById<EditText>(R.id.etName).text.toString()
            val notes = findViewById<EditText>(R.id.etNotes).text.toString()

            if (orderName.isEmpty()) {
                Toast.makeText(this, "Kolom ordering name tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, ConfirmationActivity::class.java)
            intent.putExtra("FOOD_NAMES", selectedFoods.joinToString("\n") { it.name })
            intent.putExtra("TOTAL_SERVINGS", selectedFoods.joinToString("\n") { it.quantity.toString() })
            intent.putExtra("ORDER_NAME", orderName)
            intent.putExtra("NOTES", notes)
            startActivity(intent)
        }

        // Tombol kembali ke menu
        val backToMenuButton = findViewById<Button>(R.id.kembalikeMenu)
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
