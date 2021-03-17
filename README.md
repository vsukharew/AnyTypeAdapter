# AnyTypeAdapter

### The RecyclerView Adapter that is able to compose different view types in one list

<p>
<img src="https://j.gifs.com/NLPR98.gif" alt="List with pagination" width="320"/>
<img src="https://j.gifs.com/NLPRN6.gif" alt="Dynamic content" width="320"/>
<img src="https://j.gifs.com/ANygYB.gif" alt="Static content" width="320"/>
<img src="https://j.gifs.com/wVPv4m.gif" alt="Static content - clicks" width="320"/>
</p>

This adapter is: 
- Type-safe and multitype
- Written once and used for each list in a project (including DiffUtil)
- Without the need to create additional interfaces or models-wrappers. Only those ones you really need

### Under the hood: 
- Kotlin (with using coroutines)
- Android View Binding

## Dependencies:
In your module level `build.gradle` add:
``` 
android {
    ...
    buildFeatures {
        viewBinding true
    }
    ...
}

dependencies {
    ...
    implementation 'io.github.vsukharew:anytypeadapter:x.y.z@aar'
    ...
}
```
You might want to add the library's dependencies. In this case add: 
```
dependencies {
    ...
    implementation ('io.github.vsukharew:anytypeadapter:x.y.z@aar') {
        transitive = true
    }
    ...
}
```

## Usage:
1. Inherit```AnyTypeDelegate``` and ```AnyTypeViewHolder```
```kotlin
class TracksDelegate : AnyTypeDelegate<Track, DelegateTrackBinding, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(
        // generated view binding file
        DelegateTrackBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_track

    override fun getItemId(item: Track): String = item.id

    class Holder(
        binding: DelegateTrackBinding
    ) : AnyTypeViewHolder<Track, DelegateTrackBinding>(binding) {
        // views declaration 

        override fun bind(item: Track) {
            // bind data
        }
    }
}
```

In case of not having data to be bound inherit ```NoDataDelegate``` and ```NoDataViewHolder```
```kotlin
class TracksErrorDelegate(
    private val retryClickListener: () -> Unit
) : NoDataDelegate<DelegateTracksErrorBinding>() {

    override fun createViewHolder(itemView: View): NoDataViewHolder<DelegateTracksErrorBinding> =
        Holder(DelegateTracksErrorBinding.bind(itemView))

    override fun getItemViewType(): Int = R.layout.delegate_tracks_error

    inner class Holder(binding: DelegateTracksErrorBinding) :
        NoDataViewHolder<DelegateTracksErrorBinding>(binding) {
        init {
            binding.retryBtn.setOnClickListener {
                retryClickListener.invoke()
            }
        }
    }
}
```
2. Create adapter and delegates instances
```kotlin
//class-scope variables
val tracksDelegate = TracksDelegate()
val errorDelegate = TracksErrorDelegate() { /*retry implementation*/ }
val adapter = AnyTypeAdapter()
```
3. Create `AnyTypeCollection`, fill it with the data and pass it to adapter
```kotlin
AnyTypeCollection.Builder()
    .add(tracks, tracksDelegate)
    .addIf(errorDelegate) { error is TracksLoadingException } // combine builder methods depending on your data
    .build()
    .let { anyTypeAdapter.setCollection(it) }
```            
