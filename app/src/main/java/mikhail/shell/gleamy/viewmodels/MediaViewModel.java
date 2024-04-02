package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.repositories.MediaRepository;

public class MediaViewModel extends ViewModel {
    private final MediaRepository mediaRepository;
    private final MutableLiveData<ActionModel<Media>> mediaData;
    private final Queue<ActionModel<Media>> pendingMedia;
    private final Long userid;
    private final LifecycleOwner lifecycleOwner;
    public MediaViewModel(LifecycleOwner lifecycleOwner, Long userid)
    {
        this.lifecycleOwner = lifecycleOwner;
        this.userid = userid;
        mediaRepository = MediaRepository.getInstance(GleamyApp.getApp());
        mediaData = new MutableLiveData<>();
        pendingMedia = new LinkedList<>();
    }
    public void fetchMediaPortion(Media.Type type, Long portion_num, Observer<List<Media>> observer)
    {
        MutableLiveData<List<Media>> mediaListData = new MutableLiveData<>();
        mediaRepository.fetchMediaPortionByUserId(userid, type, portion_num, mediaListData);
        mediaListData.observe(lifecycleOwner, observer);
    }
    public void fetchMediaById(String mediaid, Observer<byte[]> observer)
    {
        MutableLiveData<byte[]> mediaData = new MutableLiveData<>();
        mediaRepository.fetchMediaById(mediaid, mediaData);
        mediaData.observe(lifecycleOwner, observer);
    }
    public void observeIncomingMedia(Observer<ActionModel<Media>> observer)
    {
        mediaData.observe(lifecycleOwner, observer);
    }
    public void postMedia(Media media, byte[] bytes)
    {

    }
    public static class Factory implements ViewModelProvider.Factory
    {
        private final LifecycleOwner lifecycleOwner;
        private final Long userid;
        public Factory(LifecycleOwner lifecycleOwner, Long userid)
        {
            this.lifecycleOwner = lifecycleOwner;
            this.userid = userid;
        }
        @Override
        public <T extends ViewModel> T create(Class<T> type) {
            return (T) new MediaViewModel(lifecycleOwner, userid);
        }
    }
}