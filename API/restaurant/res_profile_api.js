'use strict'

const express = require('express');
const bodyParser = require('body-parser');
const config = require('../config');
const router = express.Router();
const images = require('../lib/images');
const delete_from_gcloud = require('../lib/delete_image_from_gcloud');

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({
    extended: true
}));

router.use((req, res, next) => {
  res.set('Content-Type', 'text/html');
  next();
});

function getModel () {
  return require('./res_profile_method');
}

router.get('/:resid', (req, res, next) => {
  getModel().read(req.params.resid, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

router.post('/create', images.multer.array('image'), images.sendUploadToGCS_RestaurantProfile, (req,res,next) => {
  const json = req.body;
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
  const data  =  {
    resid : json.resid,
	picture: json.picture,
    userid : json.userid,
    name : json.name,
    type_food :  json.type_food,
    description : json.description,
    telephone : json.telephone,
    period : json.period,
    address : json.address,
    location :  json.location,
    picture : json.picture,
    score : 0,
    vote : 0,
    create_time : json.create_time
  }
  console.log("\n\n");
  console.log(data.picture);
  getModel().create(data, (err, entity) => {
    if(err){
      next(err);
      return;
    }
    //res.json(entity);
  });
  return res.send(data.picture);
});

router.put('/upscore/:resid', (req, res, next) => {
  const json = req.body;
    getModel().calculate_score(req.params.resid, json.score, (err, entity) => {
      if(err){
        next(err);
        return;
      }
      res.json(entity);
    })
})

router.put('/update/:resid',images.multer.array('image'),(req,res,next)=>{
	
	const json  = req.body;
	console.log(json);
	
	const old_filename = json.old_filename;
	delete_from_gcloud.deleteImageFromRestProfile (old_filename, res, next);
	
	images.sendUploadToGCS_RestaurantProfile (req, res, next);
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
	
	res_pro = {
		name : json.name,
		picture: json.picture,
		type_food :  json.type_food,
		description: json.description,
		telephone: json.telephone,
		period : json.period,
		address : json.address,
		location :  json.location,
		score : json.score,
		vote : json.vote,
		create_time : json.create_time
	}
	
	getModel().update(req.params.resid, res_pro, (err,entity) =>{
		if (err) {
			next(err);
			return;
		}
		res.json(entity);
	});
});

router.delete('/del/:resid', (req, res, next) => {
  getModel().delete(req.params.resid , (err, entity) =>{
    if(err){
      next(err);
      return;
    }
    res.json(entity);
  })
})

module.exports = router; 