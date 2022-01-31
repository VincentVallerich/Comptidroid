package fr.ensisa.vallerich.comptidroid.ui.operation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import fr.ensisa.vallerich.comptidroid.R;
import fr.ensisa.vallerich.comptidroid.database.AppDatabase;
import fr.ensisa.vallerich.comptidroid.databinding.OperationFragmentBinding;
import fr.ensisa.vallerich.comptidroid.ui.account.AccountFragment;
import fr.ensisa.vallerich.comptidroid.ui.account.AccountFragmentArgs;

public class OperationFragment extends Fragment {
    public static final String ID = "id";
    private OperationViewModel mViewModel;
    private OperationFragmentBinding binding;
    private Drawable editIcon;
    private Drawable validIcon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.operation_fragment, container, false);
        binding.setLifecycleOwner(this);

        binding.setSetLabel(new TextViewBindingAdapter.OnTextChanged() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setLabel(s.toString());
            }
        });


        binding.editNameIcon.setOnClickListener((view -> {
            if (mViewModel.getEditMode().getValue()) {
                mViewModel.save();
            }
            mViewModel.switchEditMode();
        }));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OperationViewModel.class);
        mViewModel.setOperationDao(AppDatabase.get().getOperationDao());
        mViewModel.setAccountDao(AppDatabase.get().getAccountDao());
        mViewModel.getLabel().observe(getViewLifecycleOwner(), o -> binding.label.setText(o));
        mViewModel.getEditMode().observe(getViewLifecycleOwner(), v -> editNameMode(v.booleanValue()));
        long id = AccountFragmentArgs.fromBundle(getArguments()).getId();
        if (id == 0) {
            mViewModel.createOperation();
        } else {
            mViewModel.setId(id);
        }
        binding.setVm(mViewModel);
    }

    private void editNameMode(boolean edit) {
        if (validIcon == null) {
            validIcon = ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.valid_icon);
        }

        if (editIcon == null) {
            editIcon = ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.edit_icon);
        }

        binding.label.setVisibility(edit ? View.GONE : View.VISIBLE);
        binding.editLabel.setVisibility(edit ? View.VISIBLE : View.GONE);
        binding.editNameIcon.setImageDrawable(edit ? validIcon : editIcon);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
