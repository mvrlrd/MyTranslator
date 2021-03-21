package ru.mvrlrd.mytranslator.ui.fragments.categories.add_category_dialog.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_icon.view.*

import ru.mvrlrd.mytranslator.R

private val TAG = "IconsAdapter"

class IconsAdapter(private val listener: IconAdapterListener) :
    RecyclerView.Adapter<IconsAdapter.IconHolder>()
{


     private var collection = listOf(
         IconItem(R.drawable.addressbook_96,false),
         IconItem(R.drawable.airport_96,false),
         IconItem(R.drawable.alligator_96,false),
         IconItem(R.drawable.alps_96,false),
         IconItem(R.drawable.ambulance_96,false),
         IconItem(R.drawable.android_96,false),
         IconItem(R.drawable.aperture_96,false),
         IconItem(R.drawable.aquarium_96,false),
         IconItem(R.drawable.bam_96,false),
         IconItem(R.drawable.bank_96,false),
         IconItem(R.drawable.bowling_ball_96,false),
         IconItem(R.drawable.brainstorm_skill_96,false),
         IconItem(R.drawable.broccoli_96,false),
         IconItem(R.drawable.bug_96,false),
         IconItem(R.drawable.business_96,false),
         IconItem(R.drawable.cafe_96,false),
         IconItem(R.drawable.camper_96,false),
         IconItem(R.drawable.car_crash_96,false),
         IconItem(R.drawable.cat_96,false),
         IconItem(R.drawable.champagne_96,false),
         IconItem(R.drawable.cloud_lightning_96,false),
         IconItem(R.drawable.coffee_to_go_96,false),
         IconItem(R.drawable.console_96,false),
         IconItem(R.drawable.documentary_96,false),
         IconItem(R.drawable.elbow_96,false),
         IconItem(R.drawable.energy_drink_96,false),
         IconItem(R.drawable.english_mustache_96,false),
         IconItem(R.drawable.fantasy_96,false),
         IconItem(R.drawable.fighter_jet_96,false),
         IconItem(R.drawable.flower_bouquet_96,false),
         IconItem(R.drawable.french_fries_96,false),
         IconItem(R.drawable.ghost_96,false),
         IconItem(R.drawable.hanger_96,false),
         IconItem(R.drawable.weak_person_96,false),
         IconItem(R.drawable.water_transportation_96,false),
         IconItem(R.drawable.walter_white_96,false),
         IconItem(R.drawable.ullet_camera_96,false),
         IconItem(R.drawable.terrorists_96,false),
         IconItem(R.drawable.tent_96,false),
         IconItem(R.drawable.superstar_96,false),
         IconItem(R.drawable.sport_96,false),
         IconItem(R.drawable.soccer_ball_96,false),
         IconItem(R.drawable.smoking_96,false),
         IconItem(R.drawable.waste_separation_96,false),
         IconItem(R.drawable.nature_care_96,false),
         IconItem(R.drawable.large_tree_96,false),
         IconItem(R.drawable.futurama_bender_96,false)
     )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_icon, parent, false)


        return IconHolder(
            view, listener
        )
//        TranslationHolder(parent.inflate(R.layout.recycler_item))
    }


    override fun onBindViewHolder(holder: IconHolder, position: Int) {

         holder.bind(collection[position])
        holder.itemView.iconImageView.setOnClickListener {
            var message=""
            message = when (collection[position].isChecked){
                true -> {
                    holder.itemView.iconImageView.isSelected = false
                    "unselected"
                }
                false -> {
                    holder.itemView.iconImageView.isSelected = true
                    letOnlyOneMarkerBe(holder, position)
                    listener.onIconClicked(collection[position].drawableId)
                    "selected"

                }
            }
            collection[position].isChecked = !collection[position].isChecked
            Log.e(TAG, " ${collection[position].drawableId}  now  ${collection[position].isChecked}   $message    $position")
        }

    }

    private fun letOnlyOneMarkerBe(holder: IconHolder, position: Int){
        for (i in collection.indices){
            if (i != position){
                collection[i].isChecked = false
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = collection.size






    class IconHolder(itemView: View, private val listener: IconAdapterListener) :
        RecyclerView.ViewHolder(itemView) {
        //        , ItemTouchHelperViewHolder {


        fun bind(iconItem: IconItem) {
            itemView.iconImageView.load(iconItem.drawableId)
            itemView.iconImageView.isSelected = iconItem.isChecked
        }
    }

        interface IconAdapterListener {
            fun onIconClicked(id: Int)
        }


    }