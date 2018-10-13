# permissioner
Permissioner is a super simple android helper class to manage permissions in android Marshmallow and newer. Permissioner can help android developers to interact with Googles recent dynamic permissions over android devices. 
As you may know working with dynamic permissions and handle it's errors is a bit complicated for those who are new to android . So i wrote a simple class to manage these issues for you.


For the beginning you just need to copy this class in your android projects java container(folder) 
https://github.com/draxdave/permissioner/blob/fix1/app/src/main/java/ir/drax/permissions/PermissonManager.java

thats it!

How to use it ? 
first of all copy Strings.xml file's content into your project's resources folder. then

//Permissoner.hasPermissions(Activity,Permission list,requestItIfNeeded)

<pre>
private String[] manifestPermissions={Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };
    
    if (Permissoner.hasPermissions(this, manifestPermissions, false)) {
        
                //permissions has been granted so
                //every thing is OK - do your stuff
                Toast.makeText(MainActivity.this, "Great!\nYour app already granted required permissions!", Toast.LENGTH_SHORT).show();

            } else {
              Permissoner.hasPermissions(this, manifestPermissions, true) //request ...
            }
    </pre>
    
    AND
    
    in order to setup Permissioner there is one step left:
    replace this text in your onActivityResult and onRequestPermissionsResult:
    
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      Permissoner.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
      }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      Permissoner.onActivityResult(this,manifestPermissions,requestCode,resultCode,data)){
    }
    
    thats it
    
    
    
    
    
    
