package com.example.fyp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DBConnection
{
        private static DBConnection Instance;
        private RequestQueue requestQueue;
        private static Context mCtx;
        private DBConnection(Context Context)
        {
            mCtx = Context;
            requestQueue = getRequestQueue();
        }

        public static  synchronized DBConnection getInstance (Context context)
        {
            if (Instance ==null)
            {
                Instance =new DBConnection(context);
            }
            return Instance;
        }

        public RequestQueue getRequestQueue() {

            if (requestQueue==null)
            {
                requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return requestQueue;

        }

        public<T> void addTorequestque(Request<T> request)
        {
            requestQueue.add(request);
        }

}
