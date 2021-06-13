package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_icon.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CategoryIconItem

private val TAG = "IconsAdapter"

class IconsAdapter(private val listener: IconAdapterListener) :
    RecyclerView.Adapter<IconsAdapter.IconHolder>() {
    private var collection = listOf(
        CategoryIconItem(R.drawable.addressbook_96, false),
        CategoryIconItem(R.drawable.airport_96, false),
        CategoryIconItem(R.drawable.alligator_96, false),
        CategoryIconItem(R.drawable.alps_96, false),
        CategoryIconItem(R.drawable.ambulance_96, false),
        CategoryIconItem(R.drawable.android_96, false),
        CategoryIconItem(R.drawable.aperture_96, false),
        CategoryIconItem(R.drawable.aquarium_96, false),
        CategoryIconItem(R.drawable.bam_96, false),
        CategoryIconItem(R.drawable.bank_96, false),
        CategoryIconItem(R.drawable.bowling_ball_96, false),
        CategoryIconItem(R.drawable.brainstorm_skill_96, false),
        CategoryIconItem(R.drawable.broccoli_96, false),
        CategoryIconItem(R.drawable.bug_96, false),
        CategoryIconItem(R.drawable.business_96, false),
        CategoryIconItem(R.drawable.cafe_96, false),
        CategoryIconItem(R.drawable.camper_96, false),
        CategoryIconItem(R.drawable.car_crash_96, false),
        CategoryIconItem(R.drawable.cat_96, false),
        CategoryIconItem(R.drawable.champagne_96, false),
        CategoryIconItem(R.drawable.cloud_lightning_96, false),
        CategoryIconItem(R.drawable.coffee_to_go_96, false),
        CategoryIconItem(R.drawable.console_96, false),
        CategoryIconItem(R.drawable.documentary_96, false),
        CategoryIconItem(R.drawable.elbow_96, false),
        CategoryIconItem(R.drawable.energy_drink_96, false),
        CategoryIconItem(R.drawable.english_mustache_96, false),
        CategoryIconItem(R.drawable.fantasy_96, false),
        CategoryIconItem(R.drawable.fighter_jet_96, false),
        CategoryIconItem(R.drawable.flower_bouquet_96, false),
        CategoryIconItem(R.drawable.french_fries_96, false),
        CategoryIconItem(R.drawable.ghost_96, false),
        CategoryIconItem(R.drawable.hanger_96, false),
        CategoryIconItem(R.drawable.weak_person_96, false),
        CategoryIconItem(R.drawable.water_transportation_96, false),
        CategoryIconItem(R.drawable.walter_white_96, false),
        CategoryIconItem(R.drawable.ullet_camera_96, false),
        CategoryIconItem(R.drawable.terrorists_96, false),
        CategoryIconItem(R.drawable.tent_96, false),
        CategoryIconItem(R.drawable.superstar_96, false),
        CategoryIconItem(R.drawable.sport_96, false),
        CategoryIconItem(R.drawable.soccer_ball_96, false),
        CategoryIconItem(R.drawable.smoking_96, false),
        CategoryIconItem(R.drawable.waste_separation_96, false),
        CategoryIconItem(R.drawable.nature_care_96, false),
        CategoryIconItem(R.drawable.large_tree_96, false),
        CategoryIconItem(R.drawable.futurama_bender_96, false)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_icon, parent, false)
        return IconHolder(view)
    }

    override fun onBindViewHolder(holder: IconHolder, position: Int) {
        holder.bind(collection[position])
        holder.itemView.iconImageView.setOnClickListener {
            var message = ""
            message = when (collection[position].isChecked) {
                true -> {
                    holder.itemView.iconImageView.isSelected = false
//                    holder.itemView.iconImageView.borderWidth = 6
//                    holder.itemView.iconImageView.borderColor =Color.BLACK
//                    listener.onIconClicked(holder.itemView,collection[position].drawableId)
                    "unselected"
                }
                false -> {
                    holder.itemView.iconImageView.isSelected = true
//                    holder.itemView.iconImageView.borderWidth =10
//                    holder.itemView.iconImageView.borderColor = Color.WHITE
                    letOnlyOneMarkerBe(position)
                    Log.e(TAG, "pos ${position} , ad pos =${holder.adapterPosition} ")

//                    listener.onIconClicked(holder.itemView,collection[position].drawableId)
                    "selected"

                }
            }

            listener.onIconClicked(holder.itemView,collection[position].drawableId)

            collection[position].isChecked = !collection[position].isChecked
//            if (holder.itemView.iconImageView.isSelected){
//                holder.itemView.iconImageView.borderWidth =10
//                holder.itemView.iconImageView.borderColor = Color.WHITE
//            }else{
//                holder.itemView.iconImageView.borderWidth =4
//                holder.itemView.iconImageView.borderColor =Color.BLACK
//            }
            Log.e(TAG, "R.drawable.id #${collection[position].drawableId} now ${collection[position].isChecked} = $message  position in list #$position")
        }
    }

    private fun letOnlyOneMarkerBe(position: Int) {

        for (i in collection.indices) {
            if (i != position) {
                collection[i].isChecked = false
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = collection.size


    class IconHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {

        fun bind(categoryIconItem: CategoryIconItem) {
            itemView.iconImageView.load(categoryIconItem.drawableId)
            itemView.iconImageView.isSelected = categoryIconItem.isChecked
        }
    }

    interface IconAdapterListener {
        fun onIconClicked(view: View,id: Int)
    }
}