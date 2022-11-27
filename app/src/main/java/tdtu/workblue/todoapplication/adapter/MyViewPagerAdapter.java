package tdtu.workblue.todoapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import tdtu.workblue.todoapplication.fragment.CalendarFragment;
import tdtu.workblue.todoapplication.fragment.TasksFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new CalendarFragment();
            case 0:
            default:
                return new TasksFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
