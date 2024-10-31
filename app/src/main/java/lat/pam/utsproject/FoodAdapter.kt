package lat.pam.utsproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private val foodList: MutableList<Food>,
    private val onItemClick: (Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]

        // Set data makanan ke tampilan
        holder.foodName.text = food.name
        holder.foodDescription.text = food.description
        holder.foodImage.setImageResource(food.imageResourceId)

        // Reset listener untuk mencegah recursive trigger
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = food.quantity > 0
        holder.quantityTextView.text = food.quantity.toString()

        Log.d("FoodAdapter", "Binding ${food.name}: quantity = ${food.quantity}")

        // Checkbox listener
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            // Hanya set quantity ke 1 jika checkbox dicentang
            food.quantity = if (isChecked) 1 else 0 // Menggunakan ternary operator untuk set quantity
            holder.quantityTextView.text = food.quantity.toString()
            onItemClick(food) // Notifikasi perubahan
        }

        // Button to decrease quantity
        holder.btnDecrease.setOnClickListener {
            if (food.quantity > 0) {
                food.quantity--
                holder.quantityTextView.text = food.quantity.toString()
                holder.checkBox.isChecked = food.quantity > 0
                onItemClick(food)
            }
        }

        // Button to increase quantity
        holder.btnIncrease.setOnClickListener {
            food.quantity++ // Hanya tambah jumlah tanpa reset
            holder.quantityTextView.text = food.quantity.toString()
            holder.checkBox.isChecked = true // Pastikan checkbox dicentang jika quantity > 0
            onItemClick(food)
        }

        // Click listener for the item view itself
        holder.itemView.setOnClickListener {
            onItemClick(food)
        }
    }


    override fun getItemCount(): Int {
        return foodList.size
    }

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodDescription: TextView = itemView.findViewById(R.id.foodDescription)
        val checkBox: CheckBox = itemView.findViewById(R.id.foodCheckBox)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
    }
}
