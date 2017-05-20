
'use strict';

const express = require('express');
const bodyParser = require('body-parser');
const config = require('../config');
const images = require('../lib/images');
const delete_from_gcloud = require('../lib/delete_image_from_gcloud');

const router = express.Router();

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({
    extended: true
}));

router.use((req, res, next) => {
  res.set('Content-Type', 'text/html');
  next();
});

function getModel () {
  return require(`./user_method`);
}

router.get('/', (req, res, next) => {
  getModel().list(10, req.query.pageToken, (err, entities, cursor) => {
    if (err) {
      next(err);
      return;
    }
    res.json({
      items: entities,
      nextPageToken: cursor
    });
  });
});

router.get('/:userid', (req, res, next) => {
  getModel().read(req.params.userid, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

// router.get('/newUserIfNotExists/:userid',images.multer.array('image'), (req, res, next) => {
	// getModel().read(req.params.userid, (err, entity) => {
    // if (err) {
      // next(err);
      // return;
    // }
    // res.json(entity);
  // });
// });


router.post('/new_user',images.multer.array('image'), images.sendUploadToGCS_UserProfile,(req, res, next) => {
	const json  = req.body;
	console.log(json);
	
	if (req.file && req.file.cloudStoragePublicUrl) {
		json.picture = req.file.cloudStoragePublicUrl;
    }
	else if(req.files)
	{
		var ls=req.files;
		json.picture = "";
		var n = ls.length;
		var i = 0;
		ls.forEach(function(item){
			if(!(++i == n))
			{
				json.picture = json.picture + item.cloudStoragePublicUrl+",";
			}
			else
			{
				json.picture = json.picture + item.cloudStoragePublicUrl;
			}
		});
	}
	
	const user = {
		userid : json.userid,
		picture: json.picture,
		name: json.name,
		picture : json.picture,
		address: json.address,
		email: json.email,
		telephone: json.telephone,
		gender: json.gender,
		favourite_type : json.favourite_type,
		age: json.age
	}
	
	console.log("\n\n");
	console.log(user.picture);
	
	getModel().create(user,(err,entities) => {
		if(err){
			next(err);
			return;
		}
		//res.json(entities);
	});

	  res.send(user.picture);
});

router.post('/newUserIfNotExists/:userid',images.multer.array('image'), (req, res, next) => {
	var read_data
	const json  = req.body;

	getModel().read(req.params.userid, (err, entity) => {
    if (err) {
      //next(err);
	  if(err.code=="404")
	  {
		images.sendUploadToGCS_UserProfile(req, res, next);
		if (req.file && req.file.cloudStoragePublicUrl) {
		json.picture = req.file.cloudStoragePublicUrl;
		}
		else if(req.files)
		{
			var ls=req.files;
			json.picture = "";
			var n = ls.length;
			var i = 0;
			ls.forEach(function(item){
				if(!(++i == n))
				{
					json.picture = json.picture + item.cloudStoragePublicUrl+",";
				}
				else
				{
					json.picture = json.picture + item.cloudStoragePublicUrl;
				}
			});
		}
	
		const user = {
			userid : json.userid,
			picture: json.picture,
			name: json.name,
			picture : json.picture,
			address: json.address,
			email: json.email,
			telephone: json.telephone,
			gender: json.gender,
			favourite_type : json.favourite_type,
			age: json.age
		}
		
		getModel().read_and_create(user,(err, entities) => {
		if(err){
			next(err);
			return;
		}
		read_data = entities;
		res.end(JSON.stringify(read_data));
		})
	  }
	  else{
		  next(err);
	  }
      return;
    }
	res.json(entity);
	});
	
});

router.put('/:userid',images.multer.array('image'), (req, res, next) => {
	const json  = req.body;
	console.log(json);
	
	const old_filename = json.old_filename;
	delete_from_gcloud.deleteImageFromUserProfile (old_filename, res, next);
	
	images.sendUploadToGCS_UserProfile (req, res, next);
	if (req.file && req.file.cloudStoragePublicUrl) {
		json.picture = req.file.cloudStoragePublicUrl;
    }
	else if(req.files)
	{
		var ls=req.files;
		json.picture = "";
		var n = ls.length;
		var i = 0;
		ls.forEach(function(item){
			if(!(++i == n))
			{
				json.picture = json.picture + item.cloudStoragePublicUrl+",";
			}
			else
			{
				json.picture = json.picture + item.cloudStoragePublicUrl;
			}
		});
	}
	const user = {		
		name: json.name,
		address: json.address,
		telephone: json.telephone,
		picture: json.picture,
		email: json.email,
		gender: json.gender,		
		favourite_type: json.favourite_type,
		age: json.age
	}
	
	getModel().update(req.params.userid, user, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.send("update successful");
  });
});

router.use((err, req, res, next) => {
  // Format error and forward to generic error handler for logging and
  // responding to the request
  err.response = {
    message: err.message,
    internalCode: err.code
  };
  next(err);
});

module.exports = router;
