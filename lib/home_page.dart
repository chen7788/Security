import 'package:flutter/material.dart';
import 'package:security/android/to_android.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    var primaryColor = Theme.of(context).primaryColor;
    return Scaffold(
        appBar: PreferredSize(
          preferredSize: Size.fromHeight(36),
          child: AppBar(
            centerTitle: true,
            title: Text("首页",
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 16,
                    fontWeight: FontWeight.bold)),
            backgroundColor: primaryColor,
            elevation: 0,
          ),
        ),
        body: Center(
          child: Row(
            children: <Widget>[
              RaisedButton(
                color: primaryColor,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(Radius.circular(20))),
                child: Text("跳转android界面",
                    style: TextStyle(fontSize: 16, color: Colors.white)),
                onPressed: () {
                  ToAndroid.goNativeWithValue('register',null);
                },
              ),
              RaisedButton(
                color: primaryColor,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(Radius.circular(20))),
                child: Text("监听android返回数据",
                    style: TextStyle(fontSize: 16, color: Colors.white)),
                onPressed: () {
                },
              ),
              Text("android返回数据",
                  style: TextStyle(fontSize: 16, color: Colors.white)),
              RaisedButton(
                color: primaryColor,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(Radius.circular(20))),
                child: Text("跳转ios界面",
                    style: TextStyle(fontSize: 16, color: Colors.white)),
                onPressed: () {
                },
              ),
              RaisedButton(
                color: primaryColor,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.all(Radius.circular(20))),
                child: Text("监听ios界面返回数据",
                    style: TextStyle(fontSize: 16, color: Colors.white)),
                onPressed: () {
                },
              ),
              Text("ios返回数据",
                  style: TextStyle(fontSize: 16, color: Colors.white))
            ],
          )
        )
    );
  }
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    ToAndroid.requestPermission();
  }
}
