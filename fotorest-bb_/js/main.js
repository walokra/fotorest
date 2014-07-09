var imgur_endpoint = 'https://api.imgur.com/3/gallery/hot/viral/0.json';
var imgur_gallery_endpoint = 'https://api.imgur.com/3/gallery/';
var imgur_gallery_album_endpoint = 'http://api.imgur.com/3/gallery/album/';
var imgur_gallery_image_endpoint = 'http://api.imgur.com/3/gallery/image/';
var IMGUR_DIRECT_IMAGE = 'http://i.imgur.com/';

// Models
var MainGallery = Backbone.Model.extend();
var GalleryAlbum = Backbone.Model.extend();
var GalleryImage = Backbone.Model.extend();

// Collection of images, with all the image data for the given endpoint. 
var MainGalleryCollection = Backbone.Collection.extend({
    model: MainGallery,
    parse: function(response) {
        return response.data;
    }
});

// Collection of gallery album
var GalleryAlbumCollection = Backbone.Collection.extend({
    model: GalleryAlbum,
    parse: function(response) {
        return response.data;
    }
});

// Collection of gallery image
var GalleryImageCollection = Backbone.Collection.extend({
    model: GalleryImage,
    parse: function(resp) {
        return resp.data;
    }
});

//
// Views
//

// Default eventsview for events collection's view
var MainGalleryView = Backbone.View.extend({
    className : 'images',
    initialize:function () {
        this.model.bind("reset", this.render, this);
    },
    render:function (event) {
        // we don't want any old stuff there if we render this multiple times.
        this.$el.empty();
        //loop through each model, and render them separately
        _.each(this.model.models, this.renderOne, this);
        return this;
    },
    renderOne : function(data) {
        //console.log(JSON.stringify(data))
        // init the ImageView and passed in its model here
        var view = new MainGalleryImageView({
            model : data
        });
        this.$el.append(view.render().el);
    }
});

// use ImageView to render all of the images in the gallery. We need to create a collection view. 
// This is the same as a normal view, but you’ll be passing a collection to it. 
// The render function will know how to deal with an array rather than single model.
var MainGalleryImageView = Backbone.View.extend({
    className  : 'image',
    initialize:function () {
        this.model.bind("reset", this.render, this);
        this.model.bind("change", this.render, this);
        this.model.bind("destroy", this.close, this);
    },
    template:_.template($('#tpl-gallery').html()),
    render : function(event) {
        //console.log(JSON.stringify(this.model));
        //$(this.el).html(this.template(this.model.toJSON()));
        var attributes = this.model.toJSON();
        var imageId = attributes.id;    
        if (attributes.is_album) {
            imageId = attributes.cover;
        }
        attributes.imageId = imageId;
        this.$el.html(this.template(attributes));
        return this;
    },
    close:function () {
        $(this.el).unbind();
        $(this.el).remove();
    }
});

// Gallery image's details view        
var GalleryAlbumView = Backbone.View.extend({
    template:_.template($('#tpl-galleryalbum').html()),
    
    initialize:function () {
        this.model.bind("change", this.render, this);
    },
    
    render:function (event) {
        // we don't want any old stuff there if we render this multiple times.
        this.$el.empty();
        //loop through each model, and render them separately
        _.each(this.model.models, this.renderOne, this);
        return this;
    },
    renderOne : function(data) {
        //console.log(JSON.stringify(data))
        var attributes = data.toJSON();
        //console.log("attributes: " + JSON.stringify(attributes));
        if (attributes.is_album === true) {
            this.$el.html(this.template(attributes));
            //console.log("is_album");
            var images = attributes.images;
            //console.log("is_album: " + JSON.stringify(images));
            _.each(images, this.renderImage, this);
        } else {
            var view = new ImageView({
                model: attributes
            });
            this.$el.append(view.render().el);
        }
        return this;
    },
    renderImage: function(data) {
        //console.log("renderImage: " + JSON.stringify(data));
        var view = new ImageView({
            model: data
        });
        this.$el.append(view.render().el);
    },
    events:{
        "change input":"change"
    },
    change:function (event) {
        var target = event.target;
        console.log('changing ' + target.id + ' from: ' + target.defaultValue + ' to: ' + target.value);
    }
});

var ImageView = Backbone.View.extend({
    className  : 'image',
    initialize : function() {
        
    },
    template:_.template($('#tpl-image').html()),
    render : function() {
        this.$el.html(this.template(this.model));
        return this;
    }
});


//
// Fetching gallery data from imgur REST API
//

// Sending authentication header
// Either with headers/beforeSend (1)
// Or by modifying Backbone.sync  (2)
// (1)
/* 
sendAuthentication = function(xhr) {
    xhr.setRequestHeader('Authorization', 'Client-ID 000000000000000')
}
mainGallery.fetch({
    //headers: {'Authorization' :'Client-ID 000000000000000'},
    beforeSend: sendAuthentication
});
*/

// (2)
// Store a version of Backbone.sync to call from the modified version we create
var backboneSync = Backbone.sync;
Backbone.sync = function (method, model, options) {
    options.headers = {
        // Set the 'Authorization' header
        'Authorization': 'Client-ID 0df26e2cd41441a'
    };

    // Call the stored original Backbone.sync method with extra headers argument added
    backboneSync(method, model, options);
};

var mainGalleryView;
var galleryAlbumView;

var ImgurRouter = Backbone.Router.extend({
    // https://api.imgur.com/3/gallery/{section}
    routes : {
        'gallery/:id' : 'showGalleryAlbum',
        'g/:section' : 'showGallery',
        '' : 'showMainGallery'
    },
    initialize : function() {
    },

    showGalleryAlbum : function(id) {
        //console.log("showGalleryAlbum=" + id);

        // Instantiating GalleryAlbumCollection
        this.galleryAlbumCollection = new GalleryAlbumCollection();
        this.galleryAlbumCollection.url = imgur_gallery_endpoint + id;
        var self = this;
        
        this.galleryAlbumCollection.fetch({
            success: function(response, xhr) {
                console.log("showGalleryAlbum=" + id + ", success=" + xhr.status);
                //console.log("response=" + JSON.stringify(response)); // data element only
                //console.log("xhr=" + JSON.stringify(xhr)); // full response
                if (this.galleryAlbumView) this.galleryAlbumView.close();
                self.galleryAlbumView = new GalleryAlbumView({model:self.galleryAlbumCollection});
                $('.galleryalbum', this.el).html(self.galleryAlbumView.render().el);
                $('.gallerymodal').modal({
                    keyboard: true
                });
            },
            error: function(response, xhr) {
                console.log("xhr=" + JSON.stringify(xhr));
                $('div', this.el).append(response);
            }
        });
    },

    showGallery : function(section) {
        console.log("showGallery=" + section);
        // Instantiating galleryImageCollection
        this.mainGalleryCollection = new MainGalleryCollection();
        this.mainGalleryCollection.url = imgur_gallery_endpoint + section;
        var self = this;
        
        this.mainGalleryCollection.fetch({
            success: function(response, xhr) {
                console.log("success=" + xhr.status);
                //console.log("response=" + JSON.stringify(response)); // data element only
                //console.log("xhr=" + JSON.stringify(xhr)); // full response
                self.mainGalleryView = new MainGalleryView({model:self.mainGalleryCollection});
                $('.gallery', this.el).html(self.mainGalleryView.render().el);
                if (self.requestedId) self.galleryAlbumView(self.requestedId);
            },
            error: function(response, xhr) {
                console.log("xhr=" + JSON.stringify(xhr));
                $('div', this.el).append(response);
            }
        });
    },
    
    showMainGallery : function() {
        console.log("showMainGallery");
        // Instantiating galleryImageCollection
        this.mainGalleryCollection = new MainGalleryCollection();
        this.mainGalleryCollection.url = imgur_gallery_endpoint;
        var self = this;
        
        this.mainGalleryCollection.fetch({
            success: function(response, xhr) {
                console.log("success=" + xhr.status);
                //console.log("response=" + JSON.stringify(response)); // data element only
                //console.log("xhr=" + JSON.stringify(xhr)); // full response
                self.mainGalleryView = new MainGalleryView({model:self.mainGalleryCollection});
                $('.gallery', this.el).html(self.mainGalleryView.render().el);
                if (self.requestedId) self.galleryAlbumView(self.requestedId);
            },
            error: function(response, xhr) {
                console.log("xhr=" + JSON.stringify(xhr));
                $('div', this.el).append(response);
            }
        });
    }
});

var router = new ImgurRouter();
Backbone.history.start();
