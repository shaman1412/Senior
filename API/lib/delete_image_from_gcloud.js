'use strict';

const Storage = require('@google-cloud/storage');
const config = require('../config');

const CLOUD_BUCKET = config.get('CLOUD_BUCKET');
const CLOUD_BUCKET_USER_PROFILE_PIC = "user_profile_list";
const CLOUD_BUCKET_REST_PROFILE_PIC = "resteraunt_profile_list";

const storage = Storage({
  projectId: config.get('GCLOUD_PROJECT')
});
const bucket_promotion = storage.bucket(CLOUD_BUCKET);
const bucket_user_pro = storage.bucket(CLOUD_BUCKET_USER_PROFILE_PIC);
const bucket_rest_pro = storage.bucket(CLOUD_BUCKET_REST_PROFILE_PIC);


// Returns the public, anonymously accessable URL to a given Cloud Storage
// object.
// The object's ACL has to be set to public read.
// [START public_url]
function getPublicUrl (filename) {
  return `https://storage.googleapis.com/${CLOUD_BUCKET}/${filename}`;
}

function getUserProfileUrl (filename)
{
	return `https://storage.googleapis.com/${CLOUD_BUCKET_USER_PROFILE_PIC}/${filename}`;
}

function getRestProfileUrl(filename)
{
	return `https://storage.googleapis.com/${CLOUD_BUCKET_REST_PROFILE_PIC}/${filename}`;
}

// [END public_url]

// Express middleware that will automatically access to Cloud Storage.
// req.file is processed and will have two new properties:
// * ``cloudStorageObject`` the object name in cloud storage.
// * ``cloudStoragePublicUrl`` the public url to the object.
// [START process]
function deleteImageFromUserProfile (filename, res, next) {
	
	console.log(filename);
	
	const file = bucket_user_pro.file(filename);
	
	file.delete(function(err, data) {
		if (err) {
			console.log("delete error");
			// console.log(err);
			return;
		}
		else {
			console.log("Deleted successfully : ");
		}
	});
}

function deleteImageFromRestProfile (filename, res, next) {
	
	console.log(filename);
	
	const file = bucket_rest_pro.file(filename);
	
	file.delete(function(err, data) {
		if (err) {
			// console.log(err);
			next(err);
			return;
		}
		else {
			console.log("Deleted successfully : ");
		}
	});
}

function deleteImageFromPromotion (filename, res, next) {
	
	console.log(filename);
	
	const file = bucket_promotion.file(filename);
	
	file.delete(function(err, data) {
		if (err) {
			// console.log(err);
			next(err);
			return;
		}
		else {
			console.log("Deleted successfully : ");
		}
	});
}


module.exports = {
  getPublicUrl,
  getUserProfileUrl,
  getRestProfileUrl,
  deleteImageFromUserProfile,
  deleteImageFromRestProfile,
  deleteImageFromPromotion
};