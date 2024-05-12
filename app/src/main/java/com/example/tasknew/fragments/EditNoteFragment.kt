package com.example.tasknew.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tasknew.MainActivity
import com.example.tasknew.R
import com.example.tasknew.databinding.FragmentEditTaskBinding
import com.example.tasknew.model.Note
import com.example.tasknew.viewmodel.NoteViewModel




class EditNoteFragment : Fragment(R.layout.fragment_edit_task), MenuProvider{

    private var editNoteBinding: FragmentEditTaskBinding? = null
    private val binding get() = editNoteBinding!!

    private  lateinit var notesViewModel: NoteViewModel
    private  lateinit var currentNote: Note
    private val args:EditNoteFragmentArgs by navArgs()





   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editNoteBinding = FragmentEditTaskBinding.inflate(inflater,container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this , viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
       currentNote = args.note!!

        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)

        binding.editNoteFab.setOnClickListener{
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()


            if(noteTitle.isNotEmpty()){
                val note = Note(currentNote.id, noteTitle, noteDesc)
                notesViewModel.updateNote(note)
                view.findNavController().popBackStack(R.id.homeFragment, false)

            } else{
                Toast.makeText(context, "plaase enter the task title", Toast.LENGTH_SHORT).show()
            }

        }

        }

    private  fun deleteNote(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("Do you want to delete this Task?")
            setPositiveButton("Delete"){_,_ ->
                notesViewModel.deleteNote(currentNote)
                Toast.makeText(context, "Task deleted ", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment,false)

            }
            setNegativeButton("cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return  when (menuItem.itemId){
            R.id.deleteMenu ->{
                deleteNote()
                true
            } else -> false

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }

}


