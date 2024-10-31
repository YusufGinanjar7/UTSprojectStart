package lat.pam.utsproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager

class ListFoodActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private lateinit var foodList: MutableList<Food> // Menggunakan MutableList agar bisa diubah
    private lateinit var usernameTextView: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnCheckout: Button
    private var isLogoutMenuVisible = false // Menyimpan status menu logout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_food)

        // Mendapatkan username dari Intent
        val username = intent.getStringExtra("USERNAME") ?: "Yusuf"
        usernameTextView = findViewById(R.id.usernameTextView)
        usernameTextView.text = username
        btnLogout = findViewById(R.id.btnLogout)
        btnCheckout = findViewById(R.id.btnCheckout)

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Data contoh makanan
        foodList = mutableListOf(
            Food("Batagor", "Batagor asli enak dari Bandung", R.drawable.batagor),
            Food("Black Salad", "Salad segar yang dibuat secara langsung", R.drawable.black_salad),
            Food("Cappucino", "Kopi cappucino asli yang dibuat dari Kopi Arabica", R.drawable.cappuchino),
            Food("Kopi Susu", "Kopi susu yang terbuat dari susu dan kopi pilihan", R.drawable.kopi_hitam),
            Food("Nasi Goreng", "Nasi goreng dengan rempah-rempah dan topping pilihan", R.drawable.nasigoreng),
            Food("Donat", "Donat yang manis dengan topping yang bervariasi", R.drawable.donut),
            Food("Cheescake", "Cheescake yang dibuat dengan keju pilihan, menjadikannya manis dan asin yang menjadikannya dessert yang sempurna.", R.drawable.cheesecake),
            Food("Sparkling Tea", "Teh soda yang sangat menyegarkan, menjadikan hari panas mu mengcool.", R.drawable.sparkling_tea)
        )

        // Setup adapter dengan logika pemilihan makanan
        adapter = FoodAdapter(foodList) { selectedFood ->
            // Update logika untuk menyimpan pilihan makanan
            val index = foodList.indexOf(selectedFood)
            if (index != -1) {
                // Toggle jumlah makanan yang dipilih
                foodList[index].quantity = if (foodList[index].quantity > 0) 0 else 1
                adapter.notifyItemChanged(index) // Notifikasi bahwa item telah diubah
            }
        }

        recyclerView.adapter = adapter

        // Tombol Checkout berfungsi untuk checkout makanan yang dipilih
        btnCheckout.setOnClickListener {
            // Kumpulkan makanan yang dipilih (jumlah lebih dari 0)
            val selectedFoods = foodList.filter { it.quantity > 0 }

            // Periksa apakah ada makanan yang dipilih
            if (selectedFoods.isNotEmpty()) {
                val orderIntent = Intent(this, OrderActivity::class.java)
                orderIntent.putParcelableArrayListExtra("selected_food_list", ArrayList(selectedFoods)) // Kirim daftar makanan yang dipilih
                orderIntent.putExtra("USERNAME", username)
                startActivity(orderIntent)
            } else {
                Toast.makeText(this, "Silakan pilih makanan sebelum checkout.", Toast.LENGTH_SHORT).show()
            }
        }

        // Menampilkan/menghilangkan tombol Logout dengan animasi
        usernameTextView.setOnClickListener {
            isLogoutMenuVisible = !isLogoutMenuVisible
            TransitionManager.beginDelayedTransition(findViewById(R.id.main))

            if (isLogoutMenuVisible) {
                btnLogout.visibility = View.VISIBLE
            } else {
                btnLogout.visibility = View.GONE
            }
        }

        // Aksi untuk tombol Logout
        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
