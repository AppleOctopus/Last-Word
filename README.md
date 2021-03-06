# Last-Word

description: Record the last word to let everyone leave something.

## User Stories

The following **required** functionality is completed:

* T [*] User can sign in with ***Facebook*** or Google+ 
   * T [*] Request Permission of Friend List, Public Profile, Email
* A [*] Implement different scenes with dynamic background
* A [*] User can use the front camera try to record the video
* T [*] App has a centralize backend service to keep user's profile and videos
   * T [*] User Email, Viedo URL , Password, Local or remote, Category
   * T [*] API for insert video url, get list of video url by user mail.
   * T [*] Video Object include url, category, user email, password, length, format (codec)
* A [*] App can upload the user videos to Dailymotion as long as internet availible 
* A [*] User can choose different perspective to show their appreciations 
* T [*] User can view the videos they have recorded.
   * T [*] 1. User can see the first frame of their video in the video list.
* [*] User can share the video to others by system default share action.
* [*] User can copy the link in the video playing page.

The following **bonus** features are implemented:
* [*] Polish the user login screen and customize facebook login button
* [*] Polish the feeling of first open activity
* [*] Available on Google Play Store
* [] User can play their video on Dailymotion if there is no file on device
* [] User can set the password to protect their videos.
* [] Change the VideoView to ExoPlayer 
* [] User can view the videos length
* [] App only upload videos on the wifi environment

## Video Walkthrough

Here's a walkthrough of implemented user stories:

[Imgur video walkthrough](http://i.imgur.com/fLbbqlK.gifv)

https://github.com/AppleOctopus/Last-Word/blob/master/LW_1_.mp4

GIF created with [Jeff]

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [OkHTTP]
- [Retrofit]
- [gson]
- [Picasso]

## License

    Copyright AppleOctopus

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
