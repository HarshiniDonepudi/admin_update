package com.example.admin_app.doctor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin_app.PatientActivity
import com.example.admin_app.R
import com.example.admin_app.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var slots : MutableList<MutableList<Int>> = mutableListOf(mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0),mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0))
    var avl: MutableList<Int> = mutableListOf(0)
    private lateinit var dbref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        //---------------------------------
        binding.img.setOnClickListener{
          val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

        //---------------------------------
        dbref = FirebaseDatabase.getInstance().getReference("Doctors")
        setavl()
        binding.sun.setOnClickListener{
            setslots(0)
            binding.day.setText(getString(R.string.Sun))
        }
        binding.mon.setOnClickListener{
            setslots(1)
            binding.day.setText(getString(R.string.Mon))

        }
        binding.tue.setOnClickListener{
            setslots(2)
            binding.day.setText(getString(R.string.Tue))

        }
        binding.wed.setOnClickListener{
            setslots(3)
            binding.day.setText(getString(R.string.Wed))

        }
        binding.thur.setOnClickListener{
            setslots(4)
            binding.day.setText(getString(R.string.Thur))

        }
        binding.fri.setOnClickListener{
            setslots(5)
            binding.day.setText(getString(R.string.Fri))

        }
        binding.sat.setOnClickListener{
            setslots(6)
            binding.day.setText(getString(R.string.Sat))

        }
        //------------------------------------

        binding.button.setOnClickListener {
            val name = binding.editText1.text.toString()
            val id = binding.editText2.text.toString()
            val spec = binding.editText3.text.toString()
            val about = binding.editText4.text.toString()
//        val img_url = binding.editText5.editableText
            val exp = binding.editText5.text.toString()
            val lan= binding.editText6.text.toString()
            var flag : Int = 0
            if(name.isEmpty()){
                flag=2
                binding.editText1.error="Please enter name!"
                binding.editText1.requestFocus()
            }
            if(id.isEmpty()){
                flag=2
                binding.editText2.error="Please enter id!"
                binding.editText2.requestFocus()
            }
            if(spec.isEmpty()){
                flag=2
                binding.editText3.error="Please enter Spec!"
                binding.editText3.requestFocus()
            }
            if(about.isEmpty()){
                flag=2
                binding.editText4.error="Please enter about!"
                binding.editText4.requestFocus()
            }
            if(exp.isEmpty()){
                flag=2
                binding.editText5.error="Please enter exp!"
                binding.editText5.requestFocus()
            }
            if(lan.isEmpty()){
                flag=2
                binding.editText6.error="Please enter Language!"
                binding.editText6.requestFocus()
            }
            for (i in doctor){
                if(i.id.toString()==id.toString()){
                    flag=1
                    break
                }
            }
            if(flag==0){
                val data = Data(
                            id.toInt(),
                            name,
                            spec,
                            about,
                            "not done yet",
                            exp,
                            avl as ArrayList<Int>,
                            slots,
                            lan
                        )
                        Log.e("data", "$id ,$name , $about , $exp ,$lan")
                        setavl()
                        dbref.child(data.id.toString()).setValue(data)
                val toast = Toast.makeText(this," Sucessfully added",Toast.LENGTH_SHORT)
                toast.show()
                val intent = Intent(this, Doctorslist::class.java)
                startActivity(intent)

            }
            else if(flag==1){
                binding.editText2.error="ID Already Exists!"
                           binding.editText2.requestFocus()

            }



    }
    }

    var selectedPhotoUri : Uri ?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode== Activity.RESULT_OK && data!=null){
            selectedPhotoUri  =data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            val bitmapDrawable= BitmapDrawable(bitmap)
            binding.img.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private  fun setavl()
    {
        for ( i in 0..6){
            var selectedSlot = slots[i].toMutableList()
            if( selectedSlot[0]==1 || selectedSlot[1]==1 || selectedSlot[2]==1 )
            {
                if(avl.indexOf(i+1)==-1){
                    avl.add(i+1)
                }
            }
            if ( selectedSlot[0]==0 && selectedSlot[1]==0 && selectedSlot[2]==0 )
            {
                avl.remove(i+1)
            }

        }
        avl.sort()
        var text = ""
        for (i in avl){
            when(i){
                1->text+="Sun"
                2->text+=" Mon"
                3-> text+=" Tue"
                4->text+=" Wed"
                5->text+=" Thur"
                6->text+=" Fri"
                7->text+=" Sat"
            }

        }
        binding.days.text=text
        Log.e("AVL------>","avl=$avl")
    }
    private fun setslots(i: Int) {
        setavl()
            binding.slot1.isChecked=false
            binding.slot2.isChecked=false
            binding.slot3.isChecked=false
            //------------------------
            var selectedSlot = slots[i].toMutableList()
            if (selectedSlot[0]==1){
                binding.slot1.isChecked=true
            }
            if (selectedSlot[1]==1){
                binding.slot2.isChecked=true
            }
            if (selectedSlot[2]==1){
                binding.slot3.isChecked=true
            }
            binding.slot1.setOnClickListener {
                if (binding.slot1.isChecked()) {
                    selectedSlot[0] = 1
                    Log.e("Selectedslots:::>", "1True$selectedSlot")
                }
                else{
                    selectedSlot[0] = 0
                    Log.e("Selectedslots:::>", "1True$selectedSlot")
                }
                setavl()
            }
            binding.slot2.setOnClickListener {
                if (binding.slot2.isChecked()) {
                    selectedSlot[1] = 1
                    Log.e("Selectedslots:::>", "1True$selectedSlot")
                }
                else{
                    selectedSlot[1] = 0
                    Log.e("Selectedslots:::>", "1True$selectedSlot")
                }
                setavl()
            }
            binding.slot3.setOnClickListener {
                if (binding.slot3.isChecked()) {
                    selectedSlot[2] = 1
                    Log.e("Selectedslots:::>", "1True$selectedSlot")
                }
                else{
                    selectedSlot[2] = 0
                    Log.e("Selectedslots:::>", "1True$selectedSlot")
                }
                setavl()
            }

            slots[i]= selectedSlot
            Log.e("Selectedslots:::>","$selectedSlot")
        setavl()
        }
}




//---------------------------------
//var avl: MutableList<Int> = mutableListOf()
//var avlset : Set<Int> = setOf()
//var slots : MutableList<MutableList<Int>> = mutableListOf(mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0),mutableListOf(0,0,0), mutableListOf(0,0,0), mutableListOf(0,0,0))
//private lateinit var binding: ActivityMainBinding
//private lateinit var dbref : DatabaseReference
//override fun onCreate(savedInstanceState: Bundle?) {
//    binding = ActivityMainBinding.inflate(layoutInflater)
//    val view = binding.root
//    dbref = FirebaseDatabase.getInstance().getReference("Doctors")
//    val id = binding.editText1.editableText
//    val name = binding.editText2.editableText
//    val spec = binding.editText3.editableText
//    val about = binding.editText4.editableText
////        val img_url = binding.editText5.editableText
//    val exp = binding.editText5.editableText
//    val lan= binding.editText6.editableText
//
//
//
//
//    val data = Helper(2,"Karan","spec","about","dgfsdyf","5yrs",
//        arrayListOf(1,2,3), listOf(listOf(1,1,1), listOf(1,0,1), listOf(0,0,1), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//        "English,Telugu,Hindi")
//    //------------------------------------
//
//    //--------------------------------------------
//    binding.button.setOnClickListener {
//        setcolor()
//        Log.e("ARRAY:::>","$avlset")
//
////            dbref.child(data.id.toString()).setValue(data)
//    }
//    //---------------------------------------------
//    setcolor()
//    binding.sun.setOnClickListener{
//        setslots(0)
//        setcolor()
//
//        binding.day.setText(getString(R.string.Sun))
//    }
//    binding.mon.setOnClickListener{
//
//        setslots(1)
//        setcolor()
//
//        binding.day.setText(getString(R.string.Mon))
//
//    }
//    binding.tue.setOnClickListener{
//        setslots(2)
//        setcolor()
//        binding.day.setText(getString(R.string.Tue))
//
//    }
//    binding.wed.setOnClickListener{
//        setslots(3)
//        setcolor()
//        binding.day.setText(getString(R.string.Wed))
//
//    }
//    binding.thur.setOnClickListener{
//        setslots(4)
//        setcolor()
//        binding.day.setText(getString(R.string.Thur))
//
//    }
//    binding.fri.setOnClickListener{
//        setslots(5)
//        setcolor()
//        binding.day.setText(getString(R.string.Fri))
//
//    }
//    binding.sat.setOnClickListener{
//        setslots(6)
//        setcolor()
//        binding.day.setText(getString(R.string.Sat))
//
//    }
//    setcolor()
//    super.onCreate(savedInstanceState)
//    setContentView(view)
//
//
//}
//
//private fun setcolor() {
//
//    for(i in 0..6){
//        var selectedSlot = slots[i].toMutableList()
//        if ( selectedSlot[0]==1 || selectedSlot[1]==1|| selectedSlot[2]==1){
//            avl.add(i+1)
//
//        }
//        if ( selectedSlot[0]==0 && selectedSlot[1]==0&& selectedSlot[2]==0){
//            avl.remove(i+1)
//        }
//
//    }
//    avlset  = avl.sorted().toSet()
//    Log.e("avl","$avl")
////        var text = ""
////        for (i in avlset){
////            when(i){
////                1->text+="Sun"
////                2->text+=" Mon"
////                3-> text+=" Tue"
////                4->text+=" Wed"
////                5->text+=" Thur"
////                6->text+=" Fri"
////                7->text+=" Sat"
////            }
////
////        }
////        binding.days.text=text
////        for (i in avl){
////            when(i){
////                1-> binding.sun.setBackgroundColor(Color.parseColor("#FF3C963F"))
////                2-> binding.mon.setBackgroundColor(Color.parseColor("#FF3C963F"))
////                3-> binding.tue.setBackgroundColor(Color.parseColor("#FF3C963F"))
////                4-> binding.wed.setBackgroundColor(Color.parseColor("#FF3C963F"))
////                5-> binding.thur.setBackgroundColor(Color.parseColor("#FF3C963F"))
////                6-> binding.fri.setBackgroundColor(Color.parseColor("#FF3C963F"))
////                7-> binding.sat.setBackgroundColor(Color.parseColor("#FF3C963F"))
////            }
////        }
//}
//
//private fun setslots(i: Int) {
//    binding.slot1.isChecked=false
//    binding.slot2.isChecked=false
//    binding.slot3.isChecked=false
//    //------------------------
//    var selectedSlot = slots[i].toMutableList()
//    if (selectedSlot[0]==1){
//        binding.slot1.isChecked=true
//    }
//    if (selectedSlot[1]==1){
//        binding.slot2.isChecked=true
//    }
//    if (selectedSlot[2]==1){
//        binding.slot3.isChecked=true
//    }
//    binding.slot1.setOnClickListener {
//        setcolor()
//        if (binding.slot1.isChecked()) {
//            selectedSlot[0] = 1
//            Log.e("Selectedslots:::>", "1True$selectedSlot")
//        }
//        else{
//            selectedSlot[0] = 0
//            Log.e("Selectedslots:::>", "1True$selectedSlot")
//        }
//    }
//    binding.slot2.setOnClickListener {
//        setcolor()
//        if (binding.slot2.isChecked()) {
//            selectedSlot[1] = 1
//            Log.e("Selectedslots:::>", "1True$selectedSlot")
//        }
//        else{
//            selectedSlot[1] = 0
//            Log.e("Selectedslots:::>", "1True$selectedSlot")
//        }
//    }
//    binding.slot3.setOnClickListener {
//        setcolor()
//        if (binding.slot3.isChecked()) {
//            selectedSlot[2] = 1
//            Log.e("Selectedslots:::>", "1True$selectedSlot")
//        }
//        else{
//            selectedSlot[2] = 0
//            Log.e("Selectedslots:::>", "1True$selectedSlot")
//        }
//    }
//
//    slots[i]= selectedSlot
//    Log.e("Selectedslots:::>","$selectedSlot")
//
//}